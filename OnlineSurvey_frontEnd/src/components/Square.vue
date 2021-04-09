<template>
  <div class="infinite-list-wrapper" style="overflow: auto">
    <ul
      class="infinite-list"
      v-infinite-scroll="load"
      infinite-scroll-disabled="disabled"
    >
      <li
        v-for="(q, index) in questionnaireList.attrs"
        :key="index"
        class="infinite-list-item"
      >
        <el-card class="card card-item">
          <div style="height: 60px">
            <div class="item-title">
              <a :href="/detail/+ q.questionnaireId"
                ><h2>{{ q.title }}</h2></a
              >
            </div>
            <div class="item-user">
              发起人：<el-avatar :size="20" :src="renderAvatar(q.user.avatar)"></el-avatar>
              <span>{{ q.user.username }}</span>
            </div>
          </div>
          <div style="clear: both"></div>
          <div class="item-desc">
            描述：<span>{{ q.description }}</span>
          </div>
          <div style="margin-top: 1%">
            <div class="item-tags">
              <el-tag
                size="small"
                v-for="(tag, index) in q.tags"
                :key="index"
                >{{ tag.name }}</el-tag
              >
            </div>
            <div class="item-stopTime">
              截止时间:
              <span
                >{{ q.stopTime == null ? "不限制截止时间" : q.stopTime }}</span
              >
            </div>
          </div>
        </el-card>
      </li>
    </ul>
    <p v-if="loading">加载中...</p>
    <p v-if="noMore">没有更多了</p>
  </div>
</template>

<script>
import { computed, onMounted, reactive, ref } from "vue";
import { getQuestionnaireListByPublic } from "./api.js";
export default {
  setup() {
    const pages = ref(0);
    const total = ref(0);
    const currentPage = ref(1);
    const questionnaireList = reactive({
      attrs: [],
    });

    const loading = ref(false);
    const noMore = computed(() => {
      return currentPage.value >= pages.value;
    });
    const disabled = computed(() => {
      return loading.value || noMore.value;
    });
    
    const local_getQuestionnaireListByPublic = (page) => {
      getQuestionnaireListByPublic({params: {page: page}}).then((res) => {
        let data = res.data;
        console.log(data);
        if (data.code == 1) {
          pages.value = data.data.pages;
          total.value = data.data.total;
          data.data.records.forEach(element => {
              questionnaireList.attrs.push(element);
          });
          
        }
      });
    };

    const load = () => {
      loading.value = true;
      currentPage.value += 1;
      local_getQuestionnaireListByPublic(currentPage.value);
      if (noMore.value) loading.value = false;
    };

    onMounted(() => {
      local_getQuestionnaireListByPublic(currentPage.value);
    });

    const renderAvatar = (png) => {
        return require("@assets/images/"+ png)
    }

    return {
      loading,
      noMore,
      disabled,
      load,
      pages,
      total,
      currentPage,
      questionnaireList,
      renderAvatar,
    };
  },
};
</script>

<style>
a {
  text-decoration: none;
  color: #333333;
}
a:hover {
  color: cornflowerblue;
}
.infinite-list-wrapper {
  width: 80%;
  padding: 0;
  margin-left: auto;
  margin-right: auto;
}
.infinite-list {
  list-style: none;
}
.card {
  margin-bottom: 1%;
}
.el-card .el-card__body {
  padding: 1%;
}
.card-item .item-title {
  text-align: left;
  float: left;
}
.card-item .item-user {
  text-align: right;
  float: right;
  line-height: 70px;
}
.card-item .item-desc {
  text-align: left;
  font-size: 14px;
  color: #333333;
  word-break: break-all;
}
.card-item .item-stopTime {
  display: inline;
  float: right;
  color: #a9a9a9;
}
.card-item .item-tags {
  display: inline;
  text-align: left;
  float: left;
  width: 70%;
}
</style>