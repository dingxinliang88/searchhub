<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import ArticleList from "../components/ArticleList.vue";
import PictureList from "../components/PictureList.vue";
import CusDivider from "../components/CusDivider.vue";
import { SearchParam } from "../model/search";
import http from "../plugins/http";
import { Article } from "../model/article";
import { Picture } from "../model/picture";
import { BookOutlined, PictureOutlined } from "@ant-design/icons-vue";

const router = useRouter();
const route = useRoute();
const activeKey = route.params.category || "article";

const articleList = ref([] as Article[]);
const pictureList = ref([] as Picture[]);

const loadData = async (params: SearchParam) => {
  const data = (await http.get("/search/all", {
    params,
  })) as any;
  const type = params.type;
  if (type === "picture") {
    pictureList.value = data.pictureVOList;
  } else if (type === "article") {
    articleList.value = data.articleVOList;
  } else {
    pictureList.value = data.pictureVOList;
    articleList.value = data.articleVOList;
  }
};

onMounted(async () => {
  await loadData({ type: activeKey } as SearchParam);
});

const initSearchParams = {
  type: activeKey,
  searchText: "",
  pageSize: 10,
  current: 1,
};

const searchParams = ref(initSearchParams);

const onSearch = async () => {
  router.push({
    query: {
      text: searchParams.value.searchText,
    },
  });
  const searchParam = {
    searchText: searchParams.value.searchText,
    type: searchParams.value.type,
  } as SearchParam;
  await loadData(searchParam);
};

const onTabChange = async (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
  searchParams.value.type = key;
  await loadData({ ...searchParams.value } as SearchParam);
};
</script>

<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.searchText"
      placeholder="input search text"
      enter-button="搜索"
      size="large"
      @search="onSearch"
    />

    <CusDivider />

    <a-tabs v-model:activeKey="activeKey" @change="onTabChange">
      <a-tab-pane key="article">
        <template #tab>
          <span>
            <BookOutlined />
            文章
          </span>
        </template>
        <ArticleList :article-list="articleList" />
      </a-tab-pane>
      <a-tab-pane key="picture">
        <template #tab>
          <span>
            <PictureOutlined />
            图片
          </span>
        </template>
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
      <!-- TODO 扩展 -->
    </a-tabs>
  </div>
</template>

<style scoped></style>
