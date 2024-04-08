<script setup lang="ts">
import { BiliVideo } from "../model/biliVideo";
import { withDefaults } from "vue";

interface Props {
  biliVideoList: BiliVideo[];
}

const props = withDefaults(defineProps<Props>(), {
  biliVideoList: () => [],
});
</script>

<template>
  <a-list
    item-layout="horizontal"
    :grid="{ gutter: 16, xs: 1, sm: 2, md: 3, lg: 3, xl: 4, xxl: 3 }"
    :data-source="props.biliVideoList"
  >
    <template #renderItem="{ item }">
      <a-list-item>
        <a :href="item.videoUrl">
          <a-card>
            <template #cover>
              <img :src="item.pic" referrerpolicy="no-referrer" />
            </template>
            <template #title>
              <span v-html="item.title"></span>
            </template>
          </a-card>
        </a>
      </a-list-item>
    </template>
  </a-list>
</template>

<style scoped></style>
