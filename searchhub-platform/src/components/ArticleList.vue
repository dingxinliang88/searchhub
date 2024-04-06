<script setup lang="ts">
import { Article } from "../model/article";
import { withDefaults } from "vue";
import header from "../assets/header.jpg";
interface Props {
  articleList: Article[];
}

const props = withDefaults(defineProps<Props>(), {
  articleList: () => [],
});
</script>

<template>
  <a-list
    v-if="props.articleList"
    item-layout="horizontal"
    :data-source="props.articleList"
  >
    <template #renderItem="{ item }">
      <a-list-item>
        <a-list-item-meta>
          {{ item }}
          <template #title>
            <span v-html="item.title"></span>
          </template>
          <template #avatar>
            <a-avatar :src="header" />
          </template>
          <template #description>
            <p v-html="item.content" style="color: black"></p>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
  <a-empty v-else />
</template>

<style scoped></style>
