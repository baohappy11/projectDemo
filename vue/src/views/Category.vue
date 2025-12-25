<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="reset" style="margin-right: 10px;width: 240px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button @click="load" type="primary">查询</el-button>
      <el-button @click="reset" type="warning">查询</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'ADMIN'">
      <el-button @click="handleAdd" type="primary" >新增</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#000000' , backgroundColor: '#ddd'}">
        <el-table-column label="分类标题" prop="title" />
        <el-table-column label="操作"  width="100" v-if="data.user.role === 'ADMIN'" >
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 20,]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="load"
          @current-change="load"
      />
    </div>
  </div>

  <el-dialog v-model="data.formVisible" title="分类信息" width="40%" destroy-on-close>
    <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="80px" style="padding:20px 30px 10px 0">
      <el-form-item label="分类标题" prop="title" >
        <el-input v-model="data.form.title" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>

import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user')),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  rules: {
    title:[
      {required:true , message: '请输入公告标题' , trigger: 'blur'}
    ],
  }
})

const load = () => {
  request.get('/category/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if(res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const add = () => {
  formRef.value.validate((valid => {
    if (valid){
      request.post('category/add',data.form).then(res => {
        if(res.code === '200') {
          data.formVisible = false
          load()
          ElMessage.success('新增成功')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  }))
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {
  formRef.value.validate((valid => {
    if (valid){
      request.put('category/update',data.form).then(res => {
        if(res.code === '200') {
          data.formVisible = false
          load()
          ElMessage.success('更新成功')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  }))
}

const save = () => {
  data.form.id? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除数据无法恢复，您确认删除吗？' , "删除确认" , {type: 'warning'}).then(res => {
    request.delete('category/delete/' + id).then(res => {
      if(res.code === '200') {
        data.formVisible = false
        load()
        ElMessage.success('删除成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const reset = () => {
  data.title = null
  load()
}
</script>