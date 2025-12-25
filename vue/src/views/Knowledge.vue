<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="reset" style="margin-right: 10px;width: 240px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button @click="load" type="primary">查询</el-button>
      <el-button @click="reset" type="warning">查询</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px" v-if="data.user.role==='USER'">
      <el-button @click="handleAdd" type="primary" >新增</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#000000' , backgroundColor: '#ddd'}">
        <el-table-column label="知识主图" prop="img" >
          <template #default="scope">
            <el-image v-if = "scope.row.img" :preview-src-list="[scope.row.img]" :preview-teleported="true"
                      :src="scope.row.img" style="width: 40px;height: 40px;display: block;"/>
          </template>
        </el-table-column>
        <el-table-column label="知识分享标题" prop="title" />
        <el-table-column label="知识分类" prop="categoryTitle" />
        <el-table-column label="发布用户" prop="userName" />
        <el-table-column prop="content" label="分享内容">
          <template v-slot="scope">
            <el-button type="primary" @click="viewContent(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间"  />
        <el-table-column label="操作"  width="100">
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

  <el-dialog v-model="data.formVisible" title="知识分享" width="70%" destroy-on-close>
    <el-form :model="data.form" label-width="80px" style="padding:20px 30px 10px 0">
      <el-form-item>
        <el-upload
            action="http://localhost:9090/files/upload"
            :headers="{token:data.user.token}"
            :on-success="handleFileSuccess"
            list-type="picture">
          <el-button size="small" type="primary">点击上传图片</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="知识标题" prop="title" >
        <el-input v-model="data.form.title" autocomplete="off" />
      </el-form-item>
      <el-form-item label="知识分类" prop="title">
        <el-select v-model="data.form.categoryId" placeholder="请选择知识分类">
          <el-option
              v-for="item in data.categoryData"
              :key = "item.id"
              :label="item.title"
              :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="知识内容" prop="content">
        <div style="border: 1px solid #ccc">
          <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :mode="mode"
          />
          <Editor
              style="height: 500px; overflow-y: hidden;"
              v-model="data.form.content"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
          />
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="data.viewVisible" title="分享内容" width="60%" destroy-on-close>
    <div v-html="data.content" style="padding: 0 20px"></div>
  </el-dialog>

</template>

<script setup>

import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { onBeforeUnmount, ref, shallowRef, onMounted,reactive } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const formRef = ref()

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user')),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total:0,
  tableData: [],
  form: {},
  formVisible: false,
  viewVisible: false,
  content:null,
  categoryData:[],



})
/* wangEditor5 初始化开始*/
//编辑器实例，必须用shallowRef
const editorRef = shallowRef()
const mode = 'default' //或‘simple
const editorConfig = { MENU_CONF:{} }
//图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token:data.user.token,
  },
  server: 'http://localhost:9090/files/wang/upload',//服务器端图片上传接口
  fieldName: 'file' //服务器图片上传接口 参数
}
//组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
const handleCreated = (editor) => {
  editorRef.value = editor //记录editor实例，重要！
}
/*wangEditor5 初始化结束*/

const load = () => {
  request.get('/knowledge/selectPage',{
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

      request.post('knowledge/add',data.form).then(res => {
        if(res.code === '200') {
          data.formVisible = false
          load()
          ElMessage.success('新增成功')
        } else {
          ElMessage.error(res.msg)
        }
      })

}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {

      request.put('knowledge/update',data.form).then(res => {
        if(res.code === '200') {
          data.formVisible = false
          load()
          ElMessage.success('更新成功')
        } else {
          ElMessage.error(res.msg)
        }
      })

}

const save = () => {
  data.form.id? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除数据无法恢复，您确认删除吗？' , "删除确认" , {type: 'warning'}).then(res => {
    request.delete('knowledge/delete/' + id).then(res => {
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

const handleFileSuccess = (res) => {
  data.form.img = res.data
}

const viewContent = (content) => {
  data.content = content
  data.viewVisible = true
}

const loadCategory = () =>{
  request.get('category/selectAll').then(res => {
    if(res.code === '200'){
      data.categoryData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCategory()
</script>