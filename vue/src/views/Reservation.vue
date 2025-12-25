<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="reset" style="margin-right: 10px;width: 240px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-select v-model="data.status" placeholder="请选择状态" clearable style="margin-right: 10px;width: 150px">
        <el-option label="待审核" value="待审核"></el-option>
        <el-option label="已通过" value="已通过"></el-option>
        <el-option label="已拒绝" value="已拒绝"></el-option>
      </el-select>
      <el-button @click="load" type="primary">查询</el-button>
      <el-button @click="reset" type="warning">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'USER'">
      <el-button @click="handleAdd" type="primary">提交预约</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#000000' , backgroundColor: '#ddd'}">
        <el-table-column label="预约标题" prop="title" width="200" />
        <el-table-column prop="content" label="预约内容" width="250" show-overflow-tooltip />
        <el-table-column prop="reservationTime" label="预约时间" width="160" />
        <el-table-column prop="createTime" label="提交时间" width="160" />
        <el-table-column prop="userName" label="用户" width="100" v-if="data.user.role === 'ADMIN'" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === '待审核'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-else-if="scope.row.status === '已通过'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-else type="danger">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="审核备注" width="180" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <!-- 用户操作按钮 -->
            <template v-if="data.user.role === 'USER'">
              <el-button type="primary" size="small" @click="handleEdit(scope.row)" v-if="scope.row.status === '待审核'">编辑</el-button>
              <el-button type="danger" size="small" @click="del(scope.row.id)" v-if="scope.row.status === '待审核'">删除</el-button>
            </template>
            <!-- 管理员操作按钮 -->
            <template v-if="data.user.role === 'ADMIN'">
              <el-button type="success" size="small" @click="handleAudit(scope.row, '已通过')" v-if="scope.row.status === '待审核'">通过</el-button>
              <el-button type="danger" size="small" @click="handleAudit(scope.row, '已拒绝')" v-if="scope.row.status === '待审核'">拒绝</el-button>
              <el-button type="info" size="small" @click="handleView(scope.row)" v-else>查看</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="load"
          @current-change="load"
      />
    </div>
  </div>

  <!-- 提交/编辑预约对话框 -->
  <el-dialog v-model="data.formVisible" title="预约信息" width="50%" destroy-on-close>
    <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="100px" style="padding:20px 30px 10px 0">
      <el-form-item label="预约标题" prop="title">
        <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入预约标题" />
      </el-form-item>
      <el-form-item label="预约内容" prop="content">
        <el-input type="textarea" rows="4" v-model="data.form.content" autocomplete="off" placeholder="请详细说明预约内容" />
      </el-form-item>
      <el-form-item label="预约时间" prop="reservationTime">
        <el-input v-model="data.form.reservationTime" autocomplete="off" placeholder="例如：2025-12-28 14:00:00" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">提 交</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 审核对话框 -->
  <el-dialog v-model="data.auditVisible" title="审核预约" width="40%" destroy-on-close>
    <el-form :model="data.auditForm" label-width="100px" style="padding:20px 30px 10px 0">
      <el-form-item label="预约标题">
        <el-input v-model="data.auditForm.title" :disabled="true" />
      </el-form-item>
      <el-form-item label="审核结果">
        <el-tag :type="data.auditForm.status === '已通过' ? 'success' : 'danger'">{{ data.auditForm.status }}</el-tag>
      </el-form-item>
      <el-form-item label="审核备注">
        <el-input type="textarea" rows="3" v-model="data.auditForm.remark" autocomplete="off" placeholder="请输入审核意见" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.auditVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAudit">确 认</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 查看详情对话框 -->
  <el-dialog v-model="data.viewVisible" title="预约详情" width="50%" destroy-on-close>
    <el-descriptions :column="1" border>
      <el-descriptions-item label="预约标题">{{ data.viewData.title }}</el-descriptions-item>
      <el-descriptions-item label="预约内容">{{ data.viewData.content }}</el-descriptions-item>
      <el-descriptions-item label="预约时间">{{ data.viewData.reservationTime }}</el-descriptions-item>
      <el-descriptions-item label="提交时间">{{ data.viewData.createTime }}</el-descriptions-item>
      <el-descriptions-item label="用户">{{ data.viewData.userName }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag v-if="data.viewData.status === '待审核'" type="warning">{{ data.viewData.status }}</el-tag>
        <el-tag v-else-if="data.viewData.status === '已通过'" type="success">{{ data.viewData.status }}</el-tag>
        <el-tag v-else type="danger">{{ data.viewData.status }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="审核备注">{{ data.viewData.remark || '无' }}</el-descriptions-item>
    </el-descriptions>
  </el-dialog>

</template>

<script setup>

import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user')),
  title: null,
  status: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  auditForm: {},
  auditVisible: false,
  viewData: {},
  viewVisible: false,
  rules: {
    title: [
      {required: true, message: '请输入预约标题', trigger: 'blur'}
    ],
    content: [
      {required: true, message: '请输入预约内容', trigger: 'blur'}
    ],
    reservationTime: [
      {required: true, message: '请输入预约时间', trigger: 'blur'}
    ],
  }
})

const load = () => {
  request.get('/reservation/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      status: data.status
    }
  }).then(res => {
    if (res.code === '200') {
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
    if (valid) {
      request.post('reservation/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          load()
          ElMessage.success('预约提交成功')
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
    if (valid) {
      request.put('reservation/update', data.form).then(res => {
        if (res.code === '200') {
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
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', "删除确认", {type: 'warning'}).then(res => {
    request.delete('reservation/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('删除成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
  })
}

const handleAudit = (row, status) => {
  data.auditForm = {
    id: row.id,
    title: row.title,
    status: status,
    remark: ''
  }
  data.auditVisible = true
}

const submitAudit = () => {
  request.put('reservation/audit', data.auditForm).then(res => {
    if (res.code === '200') {
      data.auditVisible = false
      load()
      ElMessage.success('审核成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleView = (row) => {
  data.viewData = row
  data.viewVisible = true
}

const reset = () => {
  data.title = null
  data.status = null
  load()
}
</script>

<style scoped>
.card {
  padding: 10px;
  background-color: #fff;
  border-radius: 5px;
}
</style>
