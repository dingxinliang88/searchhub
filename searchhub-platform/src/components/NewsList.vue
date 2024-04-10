<script setup lang="ts">
import { News } from "../model/news";
import { withDefaults } from "vue";

interface Props {
  newsList: News[];
}

const props = withDefaults(defineProps<Props>(), {
  newsList: () => [],
});
</script>

<template>
  <a-list
    v-if="props.newsList"
    item-layout="vertical"
    size="large"
    :data-source="newsList"
  >
    <template #renderItem="{ item }">
      <a-list-item :key="item.title">
        <template #extra>
          <img width="272" alt="cover" :src="item.image" />
        </template>
        <a-list-item-meta :description="item.focusDate">
          <template #title>
            <a :href="item.url">{{ item.title }}</a>
          </template>
        </a-list-item-meta>
        <a :href="item.url">{{ item.brief }}</a>
      </a-list-item>
    </template>
  </a-list>
  <a-empty v-else />
</template>

<style scoped></style>
