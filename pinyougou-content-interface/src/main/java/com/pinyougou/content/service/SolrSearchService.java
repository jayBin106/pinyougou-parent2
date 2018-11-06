package com.pinyougou.content.service;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lenovo on 2018/11/6.
 */
public interface SolrSearchService {

    /**
     * 商品导入
     *
     * @throws IOException
     * @throws SolrServerException
     */
    public void importItemData() throws IOException, SolrServerException;

    /**
     * 搜索
     *
     * @param keywords
     * @return
     */
    public Map<String, Object> search(Map searchMap);

}
