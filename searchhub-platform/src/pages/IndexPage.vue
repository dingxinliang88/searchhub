<script setup lang="ts">
import { onMounted, ref, watchEffect } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ArticleList from '../components/ArticleList.vue';
import PictureList from '../components/PictureList.vue';
import CusDivider from '../components/CusDivider.vue';
import http from '../plugins/http';

const router = useRouter()
const route = useRoute()
const activeKey = route.params.category;


const articleList = ref([]);
const pictureList = ref([]);

const loadData = async (params: any) => {
    const query = {
        ...params,
        searchText: params.text
    }

    // const articleData = await http.get("/article/query/page", {
    //     params: query
    // }) as any;
    // articleList.value = articleData.records;

    // const pictureData = await http.get("/picture/query/page", {
    //     params: query
    // }) as any;
    // pictureList.value = pictureData.records;

    const data = await http.get("/search/all", {
        params: query
    }) as any;

    pictureList.value = data.pictureVOList;
    articleList.value = data.articleVOList;
}

onMounted(async () => {
    await loadData(initSearchParams);
})


const initSearchParams = {
    text: "",
    pageSize: 10,
    pageNum: 1
}

const searchPrams = ref(initSearchParams);


watchEffect(() => {
    searchPrams.value = {
        ...initSearchParams,
        text: route.query.text
    } as any
});


const onSearch = (_: string) => {
    router.push({
        query: searchPrams.value,
    })
    loadData(searchPrams.value)
};

const onTabChange = (key: string) => {
    router.push({
        path: `/${key}`,
        query: searchPrams.value
    })
    loadData(searchPrams.value)
}
</script>

<template>
    <div class="index-page">
        <a-input-search v-model:value="searchPrams.text" placeholder="input search text" enter-button="搜索" size="large"
            @search="onSearch" />

        <CusDivider />

        <a-tabs :activeKey="activeKey" @change="onTabChange">
            <a-tab-pane key="article" tab="文章">
                <ArticleList :article-list="articleList" />
            </a-tab-pane>
            <a-tab-pane key="picture" tab="图片">
                <PictureList :picture-list="pictureList" />
            </a-tab-pane>
            <!-- TODO 扩展 -->
            <!-- <a-tab-pane key="user" tab="用户">
                <UserList />
            </a-tab-pane> -->
        </a-tabs>
    </div>
</template>

<style scoped></style>
