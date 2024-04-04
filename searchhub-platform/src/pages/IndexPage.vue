<script setup lang="ts">
import { onMounted, ref, watchEffect } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ArticleList from '../components/ArticleList.vue';
import PictureList from '../components/PictureList.vue';
import UserList from '../components/UserList.vue';
import CusDivider from '../components/CusDivider.vue';
import http from '../plugins/http';

const router = useRouter()
const route = useRoute()
const activeKey = route.params.category;


const articleList = ref([]);

onMounted(async () => {
    const data = await http.get("/article/query/page", {
        params: {
            pageSize: 5
        }
    }) as any;

    console.log(data);

    articleList.value = data.records;
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
};

const onTabChange = (key: string) => {
    router.push({
        path: `/${key}`,
        query: searchPrams.value
    })
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
                <PictureList />
            </a-tab-pane>
            <a-tab-pane key="user" tab="用户">
                <UserList />
            </a-tab-pane>
        </a-tabs>
    </div>
</template>

<style scoped></style>
