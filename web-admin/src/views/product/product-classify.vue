<template>

  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增
      </el-button>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="30%">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column width="250" align="center" label="编号">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column width="180" align="center" label="分类名称">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column width="180" align="center" label="图标（未选展示）">
        <template slot-scope="{row}">
          <img :src="row.icon" style="max-height: 50px" @click="handlePictureClick(row.icon)">
        </template>
      </el-table-column>
      <el-table-column width="180" align="center" label="图标（选中展示）">
        <template slot-scope="{row}">
          <img :src="row.iconOn" style="max-height: 50px" @click="handlePictureClick(row.iconOn)">
        </template>
      </el-table-column>
      <el-table-column width="120" align="center" label="排序">
        <template slot-scope="scope">
          <span>{{ scope.row.sortOrder }}</span>
        </template>
      </el-table-column>

      <el-table-column width="120" align="center" label="状态">
        <template slot-scope="{row}">
          <el-switch
            v-model="row.enable"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleChangeStatus(row)"
          />
        </template>

      </el-table-column>

      <el-table-column align="center" label="操作" width="250">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="danger" size="mini" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageIndex" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="handleEditDialogClose">
      <el-form ref="productClassifyEditForm" :rules="productClassifyRules" :model="temp" label-position="left" label-width="140px" style="width: 400px; margin-left:50px;">
        <!--<el-form-item label="编号" prop="id">-->
        <!--<el-input v-model="temp.id" />-->
        <!--</el-form-item>-->
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="图标（未选展示）" prop="icon">
          <el-upload
            ref="uploadIcon"
            :action="uplod_action"
            list-type="picture"
            :limit="uplod_limit"
            :headers="uplod_header"
            :on-exceed="handleExLimit"
            :on-success="handleIconUploadSuccess"
            :on-preview="handlePictureCardPreview"
          >
            <img v-if="temp.iconOld" :src="temp.iconOld" width="100px">
            <i v-else class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <el-form-item label="图标（选中展示）" prop="iconOn">
          <el-upload
            ref="uploadIconOn"
            :action="uplod_action"
            list-type="picture"
            :limit="uplod_limit"
            :headers="uplod_header"
            :on-exceed="handleExLimit"
            :on-success="handleIconOnUploadSuccess"
            :on-preview="handlePictureCardPreview"
          >
            <img v-if="temp.iconOnOld" :src="temp.iconOnOld" width="100px">
            <i v-else class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input v-model="temp.sortOrder" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="temp.enable"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { fetchClassifyList, createClassify, updateClassify, deleteClassify, changeStatus } from '@/api/product'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { getToken } from '@/utils/auth'
export default {
  name: 'ProductClassify',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      uplod_limit: 1,
      dialogImageUrl: '',
      dialogVisible: false,
      list: null,
      listLoading: true,
      total: 0,
      temp: {
        id: '',
        name: '',
        icon: '',
        iconOld: '',
        iconOn: '',
        iconOnOld: '',
        sortOrder: '',
        enable: false
      },
      uplod_action: process.env.VUE_APP_TEST_BASE_API + '/api/upload/img',
      uplod_header: {
        token: getToken()
      },
      dialogFormVisible: false,
      productClassifyRules: {
        id: [{ required: true, message: '请输入编号', trigger: 'change' }],
        name: [{ required: true, message: '请输入分类名称', trigger: 'change' }],
        icon: [{ required: true, message: '请上传图片', trigger: 'change' }],
        iconOn: [{ required: true, message: '请上传图片', trigger: 'change' }],
        sortOrder: [{ required: true, message: '请输入排序码', trigger: 'change' }]
      },
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      listQuery: {
        pageIndex: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchClassifyList(this.listQuery).then(response => {
        this.list = response.data.results
        this.total = response.data.totalRecord
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    resetTemp() {
      this.temp = {
        id: '',
        name: '',
        icon: '',
        iconOld: '',
        iconOn: '',
        iconOnOld: '',
        sortOrder: '',
        enable: false
      }
    },
    handlePictureClick(url) {
      this.dialogImageUrl = url
      this.dialogVisible = true
    },
    handleExLimit() {
      this.$alert('只能上传1张图片', '上传提示', {
        confirmButtonText: '确定'
      })
    },
    handleIconUploadSuccess(response, file, fileList) {
      this.temp.icon = response.data
    },
    handleIconOnUploadSuccess(response, file, fileList) {
      this.temp.iconOn = response.data
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleEditDialogClose() {
      this.resetTemp()
      this.$refs.uploadIcon.clearFiles()
      this.$refs.uploadIconOn.clearFiles()
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['productClassifyEditForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['productClassifyEditForm'].validate((valid) => {
        if (valid) {
          createClassify(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '新建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch(() => {
            this.$notify.error({
              title: '错误',
              message: '新增失败'
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.temp.iconOld = this.temp.icon
      this.temp.iconOnOld = this.temp.iconOn
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['productClassifyEditForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['productClassifyEditForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateClassify(tempData).then(() => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.temp)
                break
              }
            }
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch(() => {
            this.$notify.error({
              title: '错误',
              message: '更新失败'
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除 ' + row.name + ' 分类?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteClassify(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        }).catch(() => {
          this.$notify.error({
            title: '错误',
            message: '删除失败'
          })
        }
        )
      })
    },
    cancelEdit(row) {
      row.title = row.originalTitle
      row.edit = false
      this.$message({
        message: 'The title has been restored to the original value',
        type: 'warning'
      })
    },
    confirmEdit(row) {
      row.edit = false
      row.originalTitle = row.title
      this.$message({
        message: 'The title has been edited',
        type: 'success'
      })
    },
    handleChangeStatus(row) {
      changeStatus(row).then(() => {
        this.$notify({
          title: '成功',
          message: '修改成功',
          type: 'success',
          duration: 2000
        })
      }).catch(() => {
        this.$notify.error({
          title: '错误',
          message: '修改失败'
        })
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
