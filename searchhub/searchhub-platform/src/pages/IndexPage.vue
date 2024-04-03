<script setup lang="ts">
import { ref, watchEffect } from 'vue';
import PostList from '../components/PostList.vue';
import PictureList from '../components/PictureList.vue';
import UserList from '../components/UserList.vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter()
const route = useRoute()
const activeKey = route.params.category || 'post';


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
        <a-input-search :value="searchPrams.text" placeholder="input search text" enter-button="Search" size="large"
            @search="onSearch" />

        <a-tabs :activeKey="activeKey" @change="onTabChange">
            <a-tab-pane key="post" tab="文章">
                <PostList />
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
