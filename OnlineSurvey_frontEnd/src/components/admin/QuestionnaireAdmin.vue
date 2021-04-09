
<template>
  <el-table
    row-key="questionnaireId"
    ref="QuestionnaireTable"
    :data="
      tableData.attrs.filter(
        (data) =>
          !search || data.title.toLowerCase().includes(search.toLowerCase())
      )
    "
  >
    <el-table-column type="index" label="序号" sortable> </el-table-column>
    <el-table-column prop="title" label="标题"> </el-table-column>
    <el-table-column prop="statusType" label="状态">
      <template #default="scope">
        <span>{{ scope.row.statusType == 1 ? "已发布" : "未发布" }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="createTime" label="创建时间"> </el-table-column>
    <el-table-column fixed="right" width="150">
      <template #header>
        <el-input v-model="search" size="mini" placeholder="输入关键字搜索" />
      </template>
      <template #default="scope">
        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
          >Edit</el-button
        >
        <el-button
          size="mini"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)"
          >Delete</el-button
        >
      </template>
    </el-table-column>
    <el-table-column type="expand">
      <template #default="props">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item label="创建者">
            <span v-if="props.row.user">{{ props.row.user.username }}</span>
          </el-form-item>
          <el-form-item label="截止时间">
            <span>{{
              props.row.stopTime == null ? "不限制截止时间" : props.row.stopTime
            }}</span>
          </el-form-item>
          <el-form-item label="可分发数量">
            <span>{{
              props.row.distribution == 0
                ? "不限制数量"
                : props.row.distribution
            }}</span>
          </el-form-item>
          <el-form-item class="tags" label="标签">
            <span v-if="props.row.tags == null">暂无标签</span>
            <el-tag v-for="tag in props.row.tags" :key="tag">{{ tag.name }}</el-tag>
          </el-form-item>
          <el-form-item label="描述">
            <span>{{ props.row.description }}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination
    :hide-on-single-page="pages == 1"
    background
    :current-page="currentPage"
    @current-change="changePage"
    layout="prev, pager, next"
    :total="total"
  >
  </el-pagination>

  <!--添加和编辑问卷弹窗-->
  <el-dialog
    :title="dialogTitle"
    v-model="dialogShow"
    :close-on-click-modal="false"
  >
    <el-form ref="form" :model="willEditWj" label-width="80px">
      <el-form-item label="问卷标题" style="width: 100%" required>
        <el-input v-model="willEditWj.title"></el-input>
      </el-form-item>
      <el-form-item label="问卷描述" style="width: 100%">
        <el-input
          v-model="willEditWj.description"
          type="textarea"
          rows="5"
        ></el-input>
      </el-form-item>
      <el-form-item label="截止时间" style="width: 100%">
        <el-input
          v-model="willEditWj.stopTime"
          type="datetime-local"
        ></el-input>
      </el-form-item>
      <el-form-item label="分发数量" style="width: 100%">
        <el-input
          v-model="willEditWj.distribution"
          type="number"
          placeholder="默认不限制数量"
        ></el-input>
      </el-form-item>
    </el-form>
    <div style="width: 100%; text-align: right">
      <el-button style="margin-left: 10px" @click="dialogShow = false"
        >取消</el-button
      >
      <el-button type="primary" style="margin-left: 10px" @click="editWjBtn"
        >确定</el-button
      >
    </div>
  </el-dialog>
</template>

<script>
import { onMounted, reactive, ref } from "vue";
import {
  deleteQuestionnaire,
  editQuestionnaire,
  getQuestionnaireList,
} from "../api.js";
import { ElMessage, ElMessageBox } from "element-plus";
import qs from "qs";
export default {
  setup() {
    const tableData = reactive({ attrs: [] });
    const search = ref("");
    const pages = ref(0);
    const total = ref(0);
    const currentPage = ref(1);
    const dialogTitle = ref("编辑问卷");
    const dialogShow = ref(false);
    const willEditWj = ref({});

    const local_getUserList = (_page) => {
      let page = { params: { page: _page } };
      getQuestionnaireList(page)
        .then((res) => {
          let data = res.data;
          // console.log(data);
          if (data.code == 1) {
            tableData.attrs = data.data.records;
            pages.value = data.data.pages;
            total.value = data.data.total;
          } else {
            ElMessage.error(data.description);
          }
        })
        .catch((error) => {
          if (error) {
            console.error(error);
            ElMessage.error("系统错误，请重试！");
          }
        });
    };

    onMounted(() => {
      local_getUserList(1);
    });

    const handleEdit = (index, row) => {
      willEditWj.value = row;
      console.log(willEditWj.value);
      dialogShow.value = true;
    };
    const handleDelete = (index, row) => {
      ElMessageBox.confirm(
        "确定删除问卷" + row.title + "? 删除后不可恢复！",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        let deleteData = {
          _method: "DELETE",
          params: { questionnaireId: row.questionnaireId },
        };
        console.log(deleteData)
        deleteQuestionnaire(deleteData)
          .then((res) => {
            let data = res.data;
            if (data.code == 1) {
              ElMessage.success("删除成功！");
              local_getUserList(currentPage.value);
            } else {
              ElMessage.warning(data.description);
            }
          })
          .catch((error) => {
            if (error) {
              console.error(error);
              ElMessage.error("系统错误，请重试！");
            }
          });
      });
    };

    const editWjBtn = () => {
      let editWjData = willEditWj.value;
      editQuestionnaire(qs.stringify(editWjData))
        .then((res) => {
          let data = res.data;
          // console.log(data)
          if (data.code == 1) {
            ElMessage.success("修改成功!");
            local_getUserList(currentPage.value);
          }
        })
        .catch((error) => {
          if (error) {
            console.error(error);
            ElMessage.error("系统错误，请重试！");
          }
        });
      dialogShow.value = false;
    };

    const changePage = (value) => {
      currentPage.value = value;
      local_getUserList(value);
    };

    return {
      tableData,
      search,
      pages,
      currentPage,
      total,
      dialogTitle,
      dialogShow,
      willEditWj,
      handleEdit,
      handleDelete,
      changePage,
      editWjBtn,
    };
  },
};
</script>

<style scoped>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.tags {
  width: inherit;
}
</style>
