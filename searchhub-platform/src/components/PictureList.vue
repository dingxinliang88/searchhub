<script setup lang="ts">
import { Picture } from '../model/picture';
import { withDefaults } from 'vue';


interface Props {
    pictureList: Picture[];
}

const props = withDefaults(defineProps<Props>(), {
    pictureList: () => []
})

</script>

<template>
    <a-list :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }" :data-source="props.pictureList">
        <template #renderItem="{ item }">
            <a-list-item>
                <a-card hoverable style="width: 240px">
                    <template #cover>
                        <img alt="img" :src="item.url" />
                    </template>
                    <a-card-meta :title="item.title" />
                </a-card>

            </a-list-item>
        </template>
    </a-list>

</template>

<style scoped></style>
