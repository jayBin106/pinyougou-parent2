package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.content.service.SolrSearchService;
import com.pinyougou.dao.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2018/11/6.
 */
@Service(version = "1.0.0")
public class SolrSearchServiceImpl implements SolrSearchService {
    @Autowired
    TbItemMapper itemMapper;
    @Autowired
    private SolrClient client;


    @Override
    public void importItemData() throws IOException, SolrServerException {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//已审核
        List<TbItem> itemList = itemMapper.selectByExample(example);
        System.out.println("===商品列表===");
        for (TbItem tbItem : itemList) {
//            Map specMap = JSON.parseObject(tbItem.getSpec());//将spec字段中的json字符串转换为map
//            tbItem.setSpecMap(specMap);//给带注解的字段赋值
            System.out.println(tbItem.getTitle());
        }
        client.addBeans(itemList);
        client.commit();
        System.out.println("商品导入完成。。。");
    }

    /**
     * 搜索
     *
     * @param str@return
     */
    @Override
    public Map<String, Object> search(Map searchMap) {
        try {
            Object o = searchMap.get("keywords");
            SolrQuery params = new SolrQuery();
            //查询条件, 这里的 q 对应 下面图片标红的地方
            params.set("q", o == null ? "" : o.toString());
            //过滤条件
//            params.set("fq", "item_price:[100 TO 100000]");
            //排序
//            params.addSort("item_price", SolrQuery.ORDER.asc);
            //分页
            params.setStart(0);
            params.setRows(20);
            //默认域
            params.set("df", "item_keywords");
            //只查询指定域
            params.set("fl", "id,item_title,item_price,item_image,item_brand");
            //高亮
            //打开开关
            params.setHighlight(true);
            //指定高亮域
            params.addHighlightField("item_title");
//            //设置前缀
//            params.setHighlightSimplePre("<em style='color:red'>");
//            //设置后缀
//            params.setHighlightSimplePost("</em>");

            QueryResponse queryResponse = client.query(params);
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            SolrDocumentList results = queryResponse.getResults();
            for (int i = 0; i < results.size(); i++) {
                /*将价格字段转换*/
                SolrDocument entries = results.get(i);
                Object item_price = entries.get("item_price");
                if (item_price != null && item_price != "") {
                    String replace = item_price.toString().replace("java.math.BigDecimal:", "");
                    entries.setField("item_price", replace);
                }
                //高亮显示
                String id = entries.get("id").toString();
                Map<String, List<String>> itemTitleMap = highlighting.get(id);
                List<String> list = itemTitleMap.get("item_title");
                entries.setField("item_title",list.get(0));
            }
            long numFound = results.getNumFound();
            System.out.println(numFound);
            HashMap<String, Object> map = new HashMap<>();
            map.put("rows", results);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
