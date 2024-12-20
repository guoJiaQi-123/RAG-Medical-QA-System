package com.tyut.config;

import com.tyut.service.ChatAssistant;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.model.zhipu.ZhipuAiEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class QdrantConfig {

    @Value("${apiKey.qwen}")
    private String apiKeyQwen;
    @Value("${apiKey.Zhipu}")
    private String apiKeyZhipu;
    @Value("${apiKey.Jina}")
    private String apiKeyJina;
    @Value("${apiKey.Tavily}")
    private String apiKeyTavily;

    /**
     * 提供模型元信息
     * @return
     */
    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKeyQwen)
                .modelName("qwen-max")
                .logRequests(true)
                .logResponses(true)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }


    /**
     * 创建qdrant客户端
     * @return
     */
    @Bean
    public QdrantClient qdrantClient() {
        QdrantGrpcClient.Builder builder = QdrantGrpcClient.newBuilder(
                "192.168.66.128",
                6334,
                false
        );
        return new QdrantClient(builder.build());
    }

    /**
     * 存储向量地方
     *
     * @return
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return QdrantEmbeddingStore.builder()
                .host("192.168.66.128")
                .port(6334)
                .collectionName("medical")
                .build();
    }

    /**
     * Embedding Model 向量模型，机器学习模型，核心功能 将高维数据映射到低维空间的技术。
     * 离散的，稀疏的数据转换连续的、密集的向量表示
     * Duration callTimeout,
     * Duration connectTimeout,
     * Duration readTimeout,
     * Duration writeTimeout
     *
     * @return
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        return ZhipuAiEmbeddingModel.builder()
                .apiKey(apiKeyZhipu)
                .callTimeout(Duration.ofSeconds(1000))
                .connectTimeout(Duration.ofSeconds(1000))
                .readTimeout(Duration.ofSeconds(1000))
                .writeTimeout(Duration.ofSeconds(1000))
                .build();
    }


    /**
     * 内容检索器  -》 内容聚合器  -》  检索增强器
     * @return
     */
    @Bean
    public ChatAssistant chatAssistant() {

        // 评分排序模型
        ScoringModel scoringModel = JinaScoringModel.builder()
                .apiKey(apiKeyJina)
                .modelName("jina-reranker-v2-base-multilingual")
                .build();


        // 内容聚合器 ReRankingContentAggregator
        ContentAggregator contentAggregator = ReRankingContentAggregator.builder()
                .scoringModel(scoringModel)
                .build();

        // 构建向量内容检索器
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore())
                .embeddingModel(embeddingModel())
                .maxResults(3)//返回最多3条结果
                .minScore(0.75)//过滤掉低于0.75评分
                .build();

        // 构建搜索引擎内容检索器
        WebSearchEngine webSearchEngine = TavilyWebSearchEngine.builder()
                .apiKey(apiKeyTavily)
                .build();
        ContentRetriever webSearchContentRetriever = WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(3)
                .build();

        // 构建检索增强器
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                // 内容检索器 向量
                .contentRetriever(contentRetriever)
                // 内容检索器  搜索引擎
                .contentRetriever(webSearchContentRetriever)
                //  内容聚合器
                .contentAggregator(contentAggregator)
                .build();

        return AiServices.builder(ChatAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel())
                .retrievalAugmentor(retrievalAugmentor)
                .build();
    }

}
