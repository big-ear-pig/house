package org.bigearpig.sys.module.es.component;

import lombok.Data;
import org.bigearpig.base.mybatis.BaseOrderItem;
import org.bigearpig.base.mybatis.BaseQo;
import org.bigearpig.sys.module.es.controller.qo.EsPage;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
@Data
@Component
public class EsComponent {
    @Resource
    private RestHighLevelClient restHighLevelClient;


    public Boolean addIndex(String indexName) {
        Assert.hasLength(indexName, "Elasticsearch exception indexName null");
        CreateIndexResponse createIndexResponse = null;
        IndicesClient indices = restHighLevelClient.indices();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        try {
            createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return createIndexResponse.isAcknowledged();
    }

    public Boolean delIndex(String indexName) {
        Assert.hasLength(indexName, "Elasticsearch exception indexName null");
        AcknowledgedResponse deleteRespone = null;
        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        try {
            deleteRespone = indices.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return deleteRespone.isAcknowledged();
    }

    public Boolean existsIndex(String indexName) {
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Boolean existsDocument(String indexName, String tableId) {
    	GetRequest getRequest = new GetRequest(indexName, tableId);
        try {
            return restHighLevelClient.exists(getRequest,  RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Boolean addDocument(String indexName, String tableId, String data) {
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(tableId);
        indexRequest.source(data, XContentType.JSON);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean delDocument(String indexName, String tableId) {
        DeleteRequest deleteRequest = new DeleteRequest(indexName, tableId);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean updateDocument(String indexName, String tableId, String data) {
        UpdateRequest updateRequest = new UpdateRequest(indexName, tableId);
        updateRequest.doc(data,XContentType.JSON);

        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchSourceBuilder generateSearchSourceBuilder( BaseQo baseQo) {
        SearchSourceBuilder searchSourceBuilder  = new SearchSourceBuilder();

        if (1 > baseQo.getCurrent()) {
            baseQo.setCurrent(1);
        }
        int size = (int) baseQo.getSize();
        int from = (int) ((baseQo.getCurrent() - 1) * size);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        if (baseQo.getOrderItemList().size() > 0) {
            for (BaseOrderItem baseOrderItem : baseQo.getOrderItemList()) {
                if (baseOrderItem.getAsc()) {
                    searchSourceBuilder.sort(baseOrderItem.getColumn(), SortOrder.ASC);
                } else {
                    searchSourceBuilder.sort(baseOrderItem.getColumn(), SortOrder.DESC);
                }
            }
        }
        return searchSourceBuilder;
    }

    public String queryPageDocument(String indexName, EsPage esPage) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        bool.must(QueryBuilders.matchAllQuery());
        bool.must(QueryBuilders.matchQuery("name", "z"));
        bool.must(QueryBuilders.multiMatchQuery("z", "name", "note"));
        bool.must(QueryBuilders.rangeQuery("tableId").from(1).to(10));

        searchSourceBuilder.query(bool);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Long total = searchResponse.getHits().getTotalHits().value;
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                String json = searchHit.getSourceAsString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }


}
