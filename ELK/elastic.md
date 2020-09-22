# 추가 / 수정
(https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-index_.html)  

PUT /<target>/_doc/<_id>  
POST /<target>/_doc/  
PUT /<target>/_create/<_id>  
POST /<target>/_create/<_id>  
다 가능하지만 _create로 하면은 이미 존재할경우 에러를 떨굼.  
하지만 _create가 없을경우는 update도 진행이됨.  

## 추가
```http request
POST/PUT /soora/_doc/1
{
    "name": "soora",
    "age": 33
}
```
```http request
POST/PUT /soora/_create/1
{
    "name": "soora",
    "age": 33
}
```
## 수정
```http request
POST/PUT /soora/_doc/1
{
    "name": "soora_update"
}
```

```http request
POST/PUT /soora/_update/1
{
    "name": "soora_update"
}
```


# 가져오기
(https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-get.html)  

GET <index>/_doc/<_id>  
HEAD <index>/_doc/<_id>  
GET <index>/_source/<_id>  
HEAD <index>/_source/<_id>  

GET 는 데이터를 가져오지만 HEAD는 존재여부만 갖고온다  
bool과 match/range등의 차이는 bool은 여러개의 조합이 가능하다.

간단하게 bool은 데이터 한 건당 하나의 query를 날리는것이라 보면 편할것 같다.  
must 묶음은 전부다 true여야 true가 return 되는듯 하다.  
그래서 아래와 같은경운 agent와 bytes와 그밑에 bool이 새로운 쿼리처럼 한개로 보면 될듯하다.  
should로 아이피중 한개만 맞을경우 거기서 true를 반환할것이고. 그러면은 3개다 true일 경우 must가 맞게된다.  

아주 간단하게 bool은 아래와같다.  
boolQuery가 true면(내부에 있는 조건문 전부가 true면) 표출한다  
```java
for(Item item : Items) {
    if(item.boolQuery()) {
        Print(item);
    }
}
```

```http request
GET /kibana_sample_data_logs/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {"agent": "Firefox"}},
        {"range": { "bytes": { "gte": 6000, "lte": 7000}}},
        {"bool": {
            "should": [
              {"match": {"ip": "15.157.237.110"}},
              {"match": {"ip": "223.87.60.27"}}
            ]
          }
        }
      ],
      "must_not": [
        {"match": {"extension": "css"}}
      ]
    }

  },
  "size": 10000
}
```  
## 가져오기
GET my-index-000001/_doc/0?_source_includes=*.id&_source_excludes=entities  
파라미터로 source include와 exclude를 지정한다  
만약 source=false로 줄경우 source는 안가져온다  


모든 데이터 검색 (score가 1.0으로 나옴)  
```http request
{
    "query": {
        "match_all": {}
    }
}
```
"mill" or "lane"이 매치되면은 표출 
```http request
{
    "query": {
        "match": {
            "address": "mill lane"
        }
    }
}
```
"mill lane"이 완전히 매치되어야 포출 
```http request
{
    "query": {
        "match_phrase": {
            "address": "mill lane"
        }
    }
}
```

"mill" 과 "lane" 이 있어야 검색됨
must는 and
should는 or
must_not은 not 
```http request
{
    "query": {
        "bool": {
            "must": {
                "address": "mill",
                "address": "lane"
            }
        }
    }
}
```
query는 score를 계산하지만 filter는 하지않고 yes / no만 한다.
must > match > agent,extension이므로  
agent = 'Firefox' and 'extension' = 'deb' 이며  
같은 bool안에 filter가 있고, 그안에 bool 이있고, 같은 원리로 match로 response는 200 or 404이다  
```http request
GET /kibana_sample_data_logs/_search
{
  "query": {
    "bool": {
      "must": [ {
          "match": {"agent": "Firefox"}
      },{
          "match": {"extension": "deb"}
      }
      ],
      "filter": {
        "bool": {
          "must": [{
          "match": {
            "response": "200 404"
          }
          }]
        }
      }
    }
  },
  "size": 10000
}
```
agent = 'Firefox' or 'extendsion' = 'css' or bytes between 6000 and 7000 인데 
점수 높음순서로 소팅한다  
```http request
GET /kibana_sample_data_logs/_search
{
  "query": {
    "bool": {
      "should": [
        {"match": {"agent": "Firefox"}},
        {"match": {"extension": "css"}},
        {"range": { "bytes": { "gte": 6000, "lte": 7000}}}
      ]
    }
  },
  "size": 10000
}
```

agent = 'Firefox' and 'extendsion' = 'css' and bytes between 6000 and 7000
```http request
GET /kibana_sample_data_logs/_search
{
  "query": {
    "must": {
      "should": [
        {"match": {"agent": "Firefox"}},
        {"match": {"extension": "css"}},
        {"range": { "bytes": { "gte": 6000, "lte": 7000}}}
      ]
    }
  },
  "size": 10000
}
```

agent = 'Firefox' and 'extendsion' != 'css' and bytes between 6000 and 7000
term 은 match_phrase와 비슷한듯하다.  
like가 아닌 =
```http request
GET /kibana_sample_data_logs/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {"agent": "Firefox"}},
        {"range": { "bytes": { "gte": 6000, "lte": 7000}}}
      ],
      "must_not": [
        {"match": {"extension": "css"}}
      ]
    }
  },
  "size": 10000
}
#--
GET /kibana_sample_data_logs/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {"agent": "Firefox"}},
        {"range": { "bytes": { "gte": 6000, "lte": 7000}}}
      ],
      "filter": {
        "bool": {
          "must_not": [
            {"term": {"extension": "css"}}
          ]
        }
      }
    }
  },
  "size": 10000
}

```
must와 should를 같이 쓰는 방법.  
minimum_should_match를 수정하면 된다.  
agent = 'Firefox' and bytes between 6000 and 8000 and (ip = '...' or ip = '') and extension != 'css'  
should,must가 동시에 있으면 값은 default가 0 이며 그외는 1이다  
```http request
GET /kibana_sample_data_logs/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {"agent": "Firefox"}},
        {"range": { "bytes": { "gte": 6000, "lte": 7000}}}
      ]
      , "should": [
        {"match": {"ip": "15.157.237.110"}},
        {"match": {"ip": "223.87.60.27"}}
      ], 
      "must_not": [
        {"match": {"extension": "css"}}
      ],
      "minimum_should_match": 1
    }
  },
  "size": 10000
}

#-- 이게 더 깔끔한듯
GET /kibana_sample_data_logs/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {"agent": "Firefox"}},
        {"range": { "bytes": { "gte": 6000, "lte": 7000}}},
        {"bool": {
            "should": [
              {"match": {"ip": "15.157.237.110"}},
              {"match": {"ip": "223.87.60.27"}}
            ]
          }
        }
      ],
      "must_not": [
        {"match": {"extension": "css"}}
      ]
    }
  },
  "size": 10000
}
```

### fields
매핑하는것인다 "city" 라는 컬럼에 서브 컬럼을 빼는 형식이다
city.raw로  
It is often useful to index the same field in different ways for different purposes. This is the purpose of multi-fields. For instance, a string field could be mapped as a text field for full-text search, and as a keyword field for sorting or aggregations:  

```http request
PUT my-index-000001
{
  "mappings": {
    "properties": {
      "city": {
        "type": "text",
        "fields": {
          "raw": { 
            "type":  "keyword"
          }
        }
      }
    }
  }
}
```

