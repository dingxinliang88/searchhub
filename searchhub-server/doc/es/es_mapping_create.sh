#!/bin/bash

addr='localhost:9200'
index_name='article_v1'

curl -X PUT "$addr/$index_name?pretty" -H 'Content-Type: application/json' -d'
{
  "aliases": {
    "article": {}
  },
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "content": {
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "create_time": {
        "type": "date"
      },
      "deleted": {
        "type": "keyword"
      }
    }
  }
}
'
