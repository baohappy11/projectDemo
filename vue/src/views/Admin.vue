<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="reset" style="width: 260px;margin-right: 5px" v-model="data.name" placeholder="请输入姓名查询" :prefix-icon="Search"></el-input>
      <el-input clearable @clear="reset" style="width: 260px;margin-right: 5px" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">查询</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger"  @click="deleteBatch">批量删除</el-button>
      <el-button type="primary" @click="handleAdd">新&nbsp&nbsp&nbsp&nbsp增</el-button>
      <el-upload
          style="display: inline-block;margin:0 10px"
          action="http://localhost:9090/admin/import"
          :show-file-list="false"
          :on-success="handleImportSuccess">
        <el-button type="success" >批量导入</el-button>
      </el-upload>
      <el-button type="info" @click="exportData">批量导出</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table  @selection-change="handleSelectionChange" :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#000000' , backgroundColor: '#ddd'}">
        <el-table-column type="selection" width="55" />
        <el-table-column label="头像" prop="avatar" >
          <template #default="scope">
            <el-image v-if = "scope.row.avatar" :preview-src-list="[scope.row.avatar]" :preview-teleported="true"
                      :src="scope.row.avatar" style="width: 40px;height: 40px;border-radius: 50%;display: block;"/>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="name" label="姓名"  />
        <el-table-column prop="phone" label="电话"  />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="操作"  width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)" ></el-button>
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

    <el-dialog v-model="data.formVisible" title="管理员信息" width="40%" destroy-on-close>

      <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0px">

        <el-upload
            action="http://localhost:9090/files/upload"
            :headers="{token:data.user.token}"
            :on-success="handleFileSuccess"
            list-type="picture">
          <el-button size="small" type="primary">点击上传头像</el-button>
        </el-upload>

        <el-form-item label="账号" >
          <el-input v-model="data.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="姓名" >
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电话" >
          <el-input v-model="data.form.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="邮箱" >
          <el-input v-model="data.form.email" autocomplete="off" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>

import {reactive, ref} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || '{}'),
  name: null,
  username: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form:{},
  formVisible: false,
  rules: {
    username: [
      {required : true , message: '请输入账号', trigger: 'blur'}
    ],
    name: [
      {required : true , message: '请输入姓名', trigger: 'blur'}
    ],
    phone: [
      {required : true , message: '请输入电话', trigger: 'blur'}
    ],
    email: [
      {required : true , message: '请输入邮箱', trigger: 'blur'}
    ],
  },
  rows: [],
  ids:[],

})

const formRef = ref()

// axios.get('http://localhost:9090/admin/selectAll').then( res => {
//   alert(res)
// })

// request.get('admin/selectAll').then((res => {
//   console.log(res)
// }))

const load = () => {
  request.get('admin/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username:data.username,
      name: data.name

    }
  }).then(res => {
    if (res.code === '200') {
      console.log(res)
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()//每次刷新页面调用

const reset = () => {
  data.name = null
  data.username = null
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const add = () => {
  formRef.value.validate((valid => {
    if (valid){
      request.post('admin/add',data.form).then(res => {
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
  data.formVisible = true
  data.form = JSON.parse(JSON.stringify(row))
}

const update = () => {
  formRef.value.validate((valid => {
    if (valid){
      request.put('admin/update',data.form).then(res => {
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
    request.delete('admin/delete/' + id).then(res => {
      if(res.code === '200') {
        data.formVisible = false
        load()
        ElMessage.success('更新成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => res)
}

const handleSelectionChange = (rows) => {
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}

const deleteBatch = (rows) => {
  if(data.rows.length===0){
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除数据无法恢复，您确认删除吗？' , "删除确认" , {type: 'warning'}).then(res => {
    request.delete('admin/deleteBatch',{data:data.rows}).then(res => {
      if(res.code === '200') {
        load()
        ElMessage.success('删除成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const exportData = () => {
  let idsStr = data.ids.join(",")
  let url = `http://localhost:9090/admin/export?username=${data.username===null ? '' : data.username }`
      + `&name=${data.name===null ? '' : data.name}`
      + `&ids=${idsStr}`
      + `&token=${data.user.token}`
  alert(idsStr)
  window.open(url)
}

const handleImportSuccess = (res) => {
  if(res.code === '200'){
    ElMessage.success("批量导入成功")
    load()
  } else {
    ElMessage.error(res.msg)
  }
}

const handleFileSuccess = (res) => {
  data.form.avatar = res.data
}

</script>