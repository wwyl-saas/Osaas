<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.shopId" placeholder="门店" clearable class="filter-item" style="width: 130px" @change="handleSelectedShop">
        <el-option v-for="item in shopsOptions" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>

      <el-select ref="shopUserSelect" v-model="listQuery.merchantUserId" placeholder="技师" clearable filterable class="filter-item" style="width: 130px" @change="handleSelectedShopUser">
        <el-option v-for="item in shopUsersOptions" :key="item.code" :label="item.name" :value="item.code" />
      </el-select>

      <el-date-picker v-model="betweenDate" type="daterange" style="width:400px;" class="filter-item" :picker-options="pickerOptions" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" />

      <el-select v-model="listQuery.appointStatus" placeholder="状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in appointmentStatusOptions" :key="item.code" :label="item.desc" :value="item.code" />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="序号" type="index" align="center" width="80" />
      <el-table-column label="门店" width="220" header-align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.shopName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="顾客名" width="150" header-align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.appointName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="技师" width="150" header-align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.merchantUserName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约手机号" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.appointMobile }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约项目" width="200" header-align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约日期" width="140" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.arriveDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到店时间" width="140" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.arriveTimeStart }} - {{ scope.row.arriveTimeEnd }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.statusName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" header-align="center" align="right" width="220" class-name="small-padding fixed-width"><!--class-name="small-padding fixed-width"-->
        <template slot-scope="{row}">
          <span v-if="row.status==0">
            <el-button type="primary" size="mini" @click="handleUpdate(row)">
              确认
            </el-button>
            <el-button type="success" size="mini" @click="handleView(row)">
              改约
            </el-button>
            <el-button type="danger" size="mini" @click="handleDelete(row)">
              取消
            </el-button>
          </span>
          <span v-if="row.status==1">
            <el-button type="success" size="mini" @click="handleView(row)">
              改约
            </el-button>
            <el-button type="danger" size="mini" @click="handleDelete(row)">
              取消
            </el-button>
          </span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageIndex" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="名称" prop="title">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="分类" prop="type">
          <el-select v-model="temp.type" class="filter-item" placeholder="Please select">
            <el-option v-for="item in appointmentStatusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="简介" prop="title">
          <el-input v-model="temp.brief" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>
        <el-form-item label="描述" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="市场价格" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="Date" prop="timestamp">
          <el-date-picker v-model="temp.timestamp" type="datetime" placeholder="Please pick a date" />
        </el-form-item>
        <el-form-item label="门店价格" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="缩略图" prop="title">
          <template>
            <el-row class="demo-avatar demo-basic">
              <el-col :span="12">
                <div class="sub-title">circle</div>
                <div class="demo-basic--circle">
                  <div class="block"><el-avatar :size="50" :src="circleUrl" /></div>
                  <div v-for="size in UrlList" :key="size" class="block">
                    <el-avatar :size="size" :src="circleUrl" />
                  </div>
                </div>
              </el-col>
            </el-row>
          </template>
        </el-form-item>
        <el-form-item label="轮播顶图" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="是否推荐" prop="title">
          <template>
            <el-radio v-model="temp.radio" label="1">是</el-radio>
            <el-radio v-model="temp.radio" label="2">否</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="上下架">
          <template>
            <el-radio v-model="temp.radio" label="1">上架</el-radio>
            <el-radio v-model="temp.radio" label="2">下架</el-radio>
          </template>
        </el-form-item>

        <el-form-item label="初始销量" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="排序值" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="关键字" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="商品规格" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>
        <el-form-item label="常见问题" prop="title">
          <el-input v-model="temp.desc" />
        </el-form-item>

        <el-form-item label="Status">
          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>

        <el-form-item label="Imp">
          <el-rate v-model="temp.importance" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="3" style="margin-top:8px;" />
        </el-form-item>
        <el-form-item label="Remark">
          <el-input v-model="temp.remark" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
        </el-form-item>
      </el-form>
      <div v-show="dialogStatus!='view'" slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchAppointmentList, getShops, getAppointmentStatus, getShopUsers, fetchPv, createArticle, updateArticle } from '@/api/appointment'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const appointmentStatusOptions = []
const shopsOptions = []
const shopUsersOptions = []

// arr to obj, such as { CN : "China", US : "USA" }
// const calendarTypeKeyValue = orderStatusOptions.reduce((acc, cur) => {
//  acc[cur.key] = cur.display_name
//  return acc
// }, {})

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
    //    typeFilter(type) {
    //      return calendarTypeKeyValue[type]
    //    }
  },
  data() {
    return {
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      squareUrl: 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png',
      UrlList: ['large', 'medium', 'small'],
      radio: '1',
      tableKey: 0,
      list: null,
      total: 0,
      betweenDate: [],
      listLoading: true,
      listQuery: {
        pageIndex: 1,
        pageSize: 20,
        appointStatus: undefined,
        startDate: undefined,
        endDate: undefined,
        merchantUserId: undefined,
        shopId: undefined
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      importanceOptions: [1, 2, 3],
      appointmentStatusOptions,
      shopsOptions,
      shopUsersOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
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
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建',
        view: '查看'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getShops()
    this.getAppointmentStatus()
  },
  methods: {
    getAppointmentStatus() {
      getAppointmentStatus().then(response => {
        this.appointmentStatusOptions = response.data
      })
    },
    getShops() {
      getShops().then(response => {
        this.shopsOptions = response.data
      })
    },
    handleSelectedShop() {
      this.listQuery.merchantUserId = undefined
      if (!this.listQuery.shopId) {
        this.shopUsersOptions = []
        return
      }
      getShopUsers(this.listQuery.shopId).then(response => {
        this.shopUsersOptions = response.data
      })
    },
    handleSelectedShopUser() {
      this.$forceUpdate()
    },
    getList() {
      this.listLoading = true
      fetchAppointmentList(this.listQuery).then(response => {
        this.list = response.data.results
        this.total = response.data.totalRecord

        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.listQuery.pageIndex = 1
      if (this.listQuery.shopId === '') this.listQuery.shopId = undefined
      if (this.listQuery.merchantUserId === '') this.listQuery.merchantUserId = undefined
      if (this.listQuery.appointStatus === '') this.listQuery.appointStatus = undefined
      this.listQuery.startDate = this.betweenDate ? this.betweenDate[0] : undefined
      this.listQuery.endDate = this.betweenDate ? this.betweenDate[1] : undefined
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
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
      this.temp = Object.assign({}, row)// copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleView(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'view'
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
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    handleDownload() {
      this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
          const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
          const data = this.formatJson(filterVal, this.list)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'table-list'
          })
          this.downloadLoading = false
        })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}`
        ? 'ascending'
        : sort === `-${key}`
          ? 'descending'
          : ''
    }
  }
}
</script>
