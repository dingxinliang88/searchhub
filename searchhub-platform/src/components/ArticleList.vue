<script setup lang="ts">
import { Article } from '../model/article';
import { withDefaults, defineProps } from 'vue';


interface Props {
    articleList: Article[];
}

const props = withDefaults(defineProps<Props>(), {
    articleList: () => []
})

</script>

<template>
    <a-list v-if="props.articleList" item-layout="horizontal" :data-source="props.articleList">
        <template #renderItem="{ item }">
            <a-list-item>
                <a-list-item-meta :description="item.content">
                    <template #title>
                        <a href="https://www.antdv.com/">{{ item.title }}</a>
                    </template>
                    <!-- <template #avatar>
                        <a-avatar src="https://joeschmoe.io/api/v1/random" />
                    </template> -->
                </a-list-item-meta>
            </a-list-item>
        </template>
    </a-list>
    <a-empty v-else />

</template>

<style scoped></style>
