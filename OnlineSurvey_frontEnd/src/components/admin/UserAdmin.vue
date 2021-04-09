
<template>
  <el-table row-key="name" ref="UserTable" :data="tableData.attrs">
    <el-table-column type="index" label="序号" sortable> </el-table-column>
    <el-table-column prop="username" label="姓名"> </el-table-column>
    <el-table-column prop="email" label="邮箱"> </el-table-column>
    <!-- <el-table-column type="password" prop="password" label="密码">
    </el-table-column> -->
    <el-table-column align="right">
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

  <!--添加和编辑用户弹窗-->
  <el-dialog
    :title="dialogTitle"
    v-model="dialogShow"
    :close-on-click-modal="false"
  >
    <el-form ref="form" :model="willEditUser" label-width="80px">
      <el-form-item label="用户ID" style="width: 100%">
        <el-input v-model="willEditUser.userId" readonly></el-input>
      </el-form-item>
      <el-form-item label="用户名" style="width: 100%">
        <el-input v-model="willEditUser.username"></el-input>
      </el-form-item>
      <el-form-item label="用户邮箱" style="width: 100%">
        <el-input v-model="willEditUser.email"></el-input>
      </el-form-item>
      <el-form-item label="用户密码" style="width: 100%">
        <el-input v-model="willEditUser.password" type="password"></el-input>
      </el-form-item>
    </el-form>
    <div style="width: 100%; text-align: right">
      <el-button style="margin-left: 10px" @click="dialogShow = false"
        >取消</el-button
      >
      <el-button type="primary" style="margin-left: 10px" @click="editUserBtn"
        >确定</el-button
      >
    </div>
  </el-dialog>
</template>

<script>
import { onMounted, reactive, ref } from "vue";
import { editUser, getUserList, deleteUser } from "../api.js";
import { ElMessage, ElMessageBox } from "element-plus";
import qs from "qs";
export default {
  setup() {
    const tableData = reactive({ attrs: [] });
    const search = ref("");
    const pages = ref(0);
    const total = ref(0);
    const currentPage = ref(1);
    const dialogTitle = ref("编辑用户");
    const dialogShow = ref(false);
    const willEditUser = ref({});

    const local_getUserList = (_page) => {
      let page = { params: { page: _page } };
      getUserList(page).then((res) => {
        let data = res.data;
        // console.log(data);
        if (data.code == 1) {
          tableData.attrs = data.data.records;
          pages.value = data.data.pages;
          total.value = data.data.total;
        } else {
          ElMessage.error(data.description);
        }
      }).catch(error => {
          if (error) {
            console.error(error)
            ElMessage.error("系统错误，请重试！")
          }
        });
    };
    onMounted(() => {
      local_getUserList(1);
    });

    const handleEdit = (index, row) => {
      willEditUser.value = row;
      dialogShow.value = true;
    };
    const handleDelete = (index, row) => {
      ElMessageBox.confirm(
        "确定删除用户" + row.username + "? 删除后不可恢复！",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        let deleteData = {
          "_method": "DELETE",
          "params": { "userId": row.userId },
        };
        deleteUser(deleteData).then((res) => {
          let data = res.data;
          if (data.code == 1) {
            ElMessage.success("删除成功！");
            local_getUserList(currentPage.value);
          } else {
            ElMessage.warning(data.description);
          }
        }).catch(error => {
          if (error) {
            console.error(error)
            ElMessage.error("系统错误，请重试！")
          }
        });
      });
    };

    const editUserBtn = () => {
      let editUserData = willEditUser.value;
      editUser(qs.stringify(editUserData)).then((res) => {
        let data = res.data;
        // console.log(data)
        if (data.code == 1) {
          ElMessage.success("修改成功!");
          local_getUserList(currentPage.value);
        }
      }).catch(error => {
          if (error) {
            console.error(error)
            ElMessage.error("系统错误，请重试！")
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
      willEditUser,
      handleEdit,
      handleDelete,
      editUserBtn,
      changePage,
    };
  },
};
</script>

<style>
</style>
