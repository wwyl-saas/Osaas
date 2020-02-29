<template>

  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
            新增
       </el-button>
     </div>
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column align="center" label="编号" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120px" align="center" label="分类名称">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="图标">
        <template slot-scope="scope">
          <svg-icon v-for="n in +scope.row.importance" :key="n" icon-class="star" class="meta-item__icon" />
        </template>
      </el-table-column>

      <el-table-column min-width="300px" label="排序">
        <template slot-scope="scope">
             <span>{{ scope.row.author }}</span>
         </template>
      </el-table-column>

      <el-table-column width="180px" align="center" label="状态">
          <el-switch
              v-model="theme"
              active-color="#13ce66"
              inactive-color="#ff4949">
           </el-switch>
       </el-table-column>

      <el-table-column align="center" label="操作" width="230">
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
        <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
          <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
            <el-form-item label="编号" prop="title">
                     <el-input v-model="temp.name" />
            </el-form-item>
            <el-form-item label="分类名称" prop="type">
              <el-select v-model="temp.type" class="filter-item" placeholder="Please select">
                <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
              </el-select>
            </el-form-item>
             <el-form-item label="图标" prop="title">
               <el-input v-model="temp.brief" />
             </el-form-item>

            <el-form-item label="排序" prop="title">
               <el-input v-model="temp.desc" />
             </el-form-item>

             <el-form-item width="180px" align="center" label="状态">
                <el-switch
                   v-model="theme"
                   active-color="#13ce66"
                   inactive-color="#ff4949">
                 </el-switch>
             </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer" >
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
import { fetchList } from '@/api/article'

export default {
  name: 'InlineEditTable',
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
      list: null,
      listLoading: true,
      temp: {
              id: undefined,
              importance: 1,
              remark: '',
              timestamp: new Date(),
              title: '',
              type: '',
              status: 'published'
            },
      dialogFormVisible: false,
      rules: {
              type: [{ required: true, message: 'type is required', trigger: 'change' }],
              timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
              title: [{ required: true, message: 'title is required', trigger: 'blur' }]
            },
      dialogStatus: '',
      textMap: {
              update: '编辑',
              create: '创建'
       },
      listQuery: {
        page: 1,
        limit: 10
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      const { data } = await fetchList(this.listQuery)
      const items = data.items
      this.list = items.map(v => {
        this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
        v.originalTitle = v.title //  will be used when user click the cancel botton
        return v
      })
      this.listLoading = false
    },
    resetTemp() {
          this.temp = {
            id: undefined,
            importance: 1,
            remark: '',
            timestamp: new Date(),
            title: '',
            status: 'published',
            type: ''
          }
    },
    handleCreate() {
          this.resetTemp()
          this.dialogStatus = 'create'
          this.dialogFormVisible = true
          this.$nextTick(() => {
            this.$refs['dataForm'].clearValidate()
          })
    },
    createData() {
          this.$refs['dataForm'].validate((valid) => {
            if (valid) {
              this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
              this.temp.author = 'vue-element-admin'
              createArticle(this.temp).then(() => {
                this.list.unshift(this.temp)
                this.dialogFormVisible = false
                this.$notify({
                  title: 'Success',
                  message: 'Created Successfully',
                  type: 'success',
                  duration: 2000
                })
              })
            }
          })
     },
    handleUpdate(row) {
          this.temp = Object.assign({}, row) // copy obj
          this.temp.timestamp = new Date(this.temp.timestamp)
          this.dialogStatus = 'update'
          this.dialogFormVisible = true
          this.$nextTick(() => {
            this.$refs['dataForm'].clearValidate()
          })
    },
    updateData() {
          this.$refs['dataForm'].validate((valid) => {
            if (valid) {
              const tempData = Object.assign({}, this.temp)
              tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
              updateArticle(tempData).then(() => {
                for (const v of this.list) {
                  if (v.id === this.temp.id) {
                    const index = this.list.indexOf(v)
                    this.list.splice(index, 1, this.temp)
                    break
                  }
                }
                this.dialogFormVisible = false
                this.$notify({
                  title: 'Success',
                  message: 'Update Successfully',
                  type: 'success',
                  duration: 2000
                })
              })
            }
          })
        },
        handleDelete(row) {
          this.$notify({
            title: 'Success',
            message: 'Delete Successfully',
            type: 'success',
            duration: 2000
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
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
