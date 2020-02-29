<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.title" placeholder="请输入商品名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-input v-model="listQuery.title" placeholder="关键字" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-select v-model="listQuery.type" placeholder="上架状态" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增
      </el-button>

    </div>
    <el-dialog :visible.sync="dialogVisible" width="30%">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
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
      <el-table-column label="编号" prop="id" sortable="custom" align="center" width="180" :class-name="getSortClass('id')">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" width="180" header-align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="缩略图" width="120" align="center">
        <template slot-scope="{row}">
          <img :src="row.briefPicUrl" style="max-height:50px" @click="handlePictureClick(row)">
        </template>
      </el-table-column>
      <el-table-column label="市场价格" width="120" header-align="center">
        <template slot-scope="scope">
          <span>￥{{ scope.row.marketPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="门店价格" width="120" header-align="center">
        <template slot-scope="scope">
          <span>￥{{ scope.row.counterPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否推荐" width="80" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.isRecommend">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="销量" width="80" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.sellVolume }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否上架" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.onSale">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="关键字" width="200" header-align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.keywords }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="270" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="success" size="mini" @click="handleView(row)">
            查看
          </el-button>
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="70%" @close="handleEditDialogClose">
      <el-form ref="dataForm" :rules="rules" :model="goodsDto.goodsMainQuery" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="名称" prop="name">
          <el-input v-model="goodsDto.goodsMainQuery.name" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="goodsDto.goodsMainQuery.categoryId" class="filter-item">
            <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" :disabled="!item.enable" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品简介" prop="goodsBrief">
          <el-input v-model="goodsDto.goodsMainQuery.goodsBrief" />
        </el-form-item>
        <el-form-item label="商品描述" prop="goodsDesc">
          <el-input
            v-model="goodsDto.goodsMainQuery.goodsDesc"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6}"
          />
        </el-form-item>
        <el-form-item label="市场价格" prop="marketPrice">
          <el-input v-model="goodsDto.goodsMainQuery.marketPrice" />
        </el-form-item>
        <el-form-item label="门店价格" prop="counterPrice">
          <el-input v-model="goodsDto.goodsMainQuery.counterPrice" />
        </el-form-item>
        <el-form-item label="缩略图" prop="briefPicUrl">
          <el-upload
            ref="uploadBriefPic"
            class="avatar-uploader"
            :headers="uplod_header"
            :action="uplod_action"
            :show-file-list="false"
            :on-success="handleUploadBriefPicSuccess"
          >
            <img v-if="goodsDto.goodsMainQuery.briefPicUrl" :src="goodsDto.goodsMainQuery.briefPicUrl.indexOf(cdnUrl)==-1?cdnUrl+goodsDto.goodsMainQuery.briefPicUrl:goodsDto.goodsMainQuery.briefPicUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
          <div class="el-upload__tip">限上传一张缩略图，以180x180像素为佳</div>

        </el-form-item>
        <el-form-item label="轮播顶图" prop="primaryPicUrls" style="width: 800px">
          <el-upload
            ref="uploadPrimaryPic"
            :action="uplod_action"
            :headers="uplod_header"
            list-type="picture-card"
            :on-success="handleUploadPrimaryPicSuccess"
            :on-remove="handleRemovePrimaryPic"
            :on-preview="handlePrimaryPicClick"
          >
            <i class="el-icon-plus" />
          </el-upload><div class="el-upload__tip">可上传多张图片，最佳尺寸为750x320像素</div>

        </el-form-item>
        <el-form-item label="商品详情图" prop="listPicUrls" style="width: 800px">
          <el-upload
            ref="uploadListPic"
            :action="uplod_action"
            :headers="uplod_header"
            list-type="picture-card"
            :on-success="handleUploadListPicSuccess"
            :on-remove="handleRemoveListPic"
            :on-preview="handlePrimaryPicClick"
          >
            <i class="el-icon-plus" />
          </el-upload>
          <div class="el-upload__tip">可上传多张图片，最佳尺寸为750x320像素</div>
        </el-form-item>
        <el-form-item label="是否推荐" prop="isRecommend">
          <template>
            <el-radio v-model="goodsDto.goodsMainQuery.isRecommend" :label="true">是</el-radio>
            <el-radio v-model="goodsDto.goodsMainQuery.isRecommend" :label="false">否</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="上下架" prop="onSale">
          <template>
            <el-radio v-model="goodsDto.goodsMainQuery.onSale" :label="true">上架</el-radio>
            <el-radio v-model="goodsDto.goodsMainQuery.onSale" :label="false">下架</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="是否热卖" prop="isHot">
          <template>
            <el-radio v-model="goodsDto.goodsMainQuery.isHot" :label="true">是</el-radio>
            <el-radio v-model="goodsDto.goodsMainQuery.isHot" :label="false">否</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="是否新品" prop="isNew">
          <template>
            <el-radio v-model="goodsDto.goodsMainQuery.isNew" :label="true">是</el-radio>
            <el-radio v-model="goodsDto.goodsMainQuery.isNew" :label="false">否</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="初始销量" prop="sellVolume">
          <el-input v-model.number="goodsDto.goodsMainQuery.sellVolume" />
        </el-form-item>
        <el-form-item label="排序值" prop="sortOrder">
          <el-input v-model.number="goodsDto.goodsMainQuery.sortOrder" />
        </el-form-item>
        <el-form-item label="关键字" prop="keywords">
          <el-input v-model="goodsDto.goodsMainQuery.keywords" />
        </el-form-item>
      </el-form>
      <el-form ref="goodsAttributeForm" :rules="rules" :model="goodsDto.goodsAttributeList" label-position="left" label-width="100px" style="width: 800px; margin-left:50px;">
        <el-form-item label="商品规格" />
        <el-form-item
          v-for="(attribute, index) in goodsDto.goodsAttributeList"
          :key="attribute.key"
        >
          <el-input v-model="goodsDto.goodsAttributeList[index].name" style="width: 150px" /><i style="width: 10px;margin: 0 20px 0 20px ">-</i><el-input v-model="goodsDto.goodsAttributeList[index].value" style="width: 150px;margin-right: 20px" /><el-button type="danger" icon="el-icon-delete" circle @click.prevent="removeAttribute(attribute)" />
        </el-form-item>
        <el-form-item>
          <el-button @click="addAttribute">新增属性</el-button>
        </el-form-item>
      </el-form>
      <el-form ref="goodsAttributeForm" :rules="rules" :model="goodsDto.goodsIssueList" label-position="left" label-width="100px" style="width: 800px; margin-left:50px;">
        <el-form-item label="常见问题" />
        <el-form-item
          v-for="(Issue, index) in goodsDto.goodsIssueList"
          :key="Issue.key"
        >
          <span>问题 {{ index+1 }}</span><el-input v-model="goodsDto.goodsIssueList[index].question" style="width: 550px;margin: 20px" /><br>
          <span>回答 {{ index+1 }}</span><el-input v-model="goodsDto.goodsIssueList[index].answer" type="textarea" :autosize="{ minRows: 2, maxRows: 6}" style="width: 550px;margin-right: 20px;margin-left: 20px" /><el-button type="danger" icon="el-icon-delete" circle @click.prevent="removeIssue(Issue)" />
        </el-form-item>
        <el-form-item>
          <el-button @click="addIssue">新增问题</el-button>
        </el-form-item>
      </el-form>
      <el-form ref="goodsShopListForm" :rules="rules" :model="goodsDto" label-position="left" label-width="100px" style="width: 800px; margin-left:50px;">
        <el-form-item label="适用门店" />
        <el-form-item>
          <template>
            <el-checkbox v-model="checkAllShop" :indeterminate="isIndeterminateShop" @change="handleCheckAllShopChange">全选</el-checkbox>
            <div style="margin: 15px 0;" />
            <el-checkbox-group v-model="goodsDto.shopList" @change="handleCheckedShopsChange">
              <el-checkbox v-for="shop in shopsOptions" :key="shop.id" :label="shop.id">{{ shop.name }}</el-checkbox>
            </el-checkbox-group>
          </template>
        </el-form-item>
        <el-form-item label="适用技师" />
        <el-form-item
          v-for="(shop, index) in goodsDto.shopList"
          :key="shop.key"
        >
          <div>{{ shopsOptions.find(item=>item.id=shop).name }}</div>
          <div>
            <template>
              <el-checkbox v-model="checkAllMerchantUsers[index]" :indeterminate="isIndeterminateShop" @change="handleCheckAllMerchantUsersChange">全选</el-checkbox>
              <div style="margin: 15px 0;" />
              <el-checkbox-group v-model="goodsDto.merchantUserList" @change="handleCheckedMerchantUsersChange">
                <el-checkbox v-for="user in shopUsersOptions[index].shopUsers" :key="user.code" :label="user.code">{{ user.name }}</el-checkbox>
              </el-checkbox-group>
            </template>
          </div>
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
import { fetchDetailList, fetchPv, createArticle, updateArticle } from '@/api/product'
import { getShops, getShopUsers } from '@/api/appointment'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { getToken } from '@/utils/auth'

const calendarTypeOptions = [
  { key: 'all', display_name: '全部' },
  { key: 'on', display_name: '上架' },
  { key: 'off', display_name: '下架' }
]
const categoryOptions = [
  { id: '1', name: '造型', enable: true },
  { id: '2', name: '护理', enable: true },
  { id: '3', name: '美甲', enable: true },
  { id: '4', name: '按摩', enable: true },
  { id: '5', name: '洗护', enable: true },
  { id: '1175479890066034689', name: '测试', enable: false }
]
const shopsOptions = []
const shopUsersOptions = []
// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

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
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      checkAllShop: false,
      checkAllMerchantUsers: [],
      isIndeterminateShop: true,
      dialogImageUrl: '',
      dialogVisible: false,
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      squareUrl: 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png',
      UrlList: ['large', 'medium', 'small'],
      radio: '1',
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        pageIndex: 1,
        pageSize: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      categoryOptions,
      uplod_action: process.env.VUE_APP_TEST_BASE_API + '/api/upload/img',
      cdnUrl: process.env.VUE_APP_CDN_URL,
      uplod_header: {
        token: getToken()
      },
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
      shopsOptions,
      shopUsersOptions,
      goodsDto: {
        goodsAttributeList: [
          {
            name: '',
            value: ''
          }
        ],
        goodsIssueList: [
          {
            answer: '',
            question: ''
          }
        ],
        goodsMainQuery: {
          briefPicUrl: '',
          categoryId: undefined,
          counterPrice: '',
          goodsBrief: '',
          goodsDesc: '',
          isHot: true,
          isNew: true,
          isRecommend: true,
          keywords: '',
          listPicUrls: '',
          marketPrice: '',
          name: '',
          onSale: true,
          primaryPicUrls: '',
          sellVolume: undefined,
          sortOrder: undefined
        },
        merchantUserList: [],
        shopList: []
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
        name: [{ required: true, message: '请输入商品名', trigger: 'change' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        goodsBrief: [{ required: true, message: '请填写商品简介', trigger: 'change' }],
        goodsDesc: [{ required: true, message: '请填写商品描述', trigger: 'change' }],
        keywords: [{ required: true, message: '请填写关键字', trigger: 'change' }],
        counterPrice: [{ required: true, message: '请输入店铺价格', trigger: 'change' }],
        marketPrice: [{ required: true, message: '请输入市场价格', trigger: 'change' }],
        sellVolume:
          [{ required: true, message: '请输入销量', trigger: 'change' },
            { type: 'number', message: '请输入数字', trigger: 'change' }],
        sortOrder:
          [{ required: true, message: '请输入排序号', trigger: 'change' },
            { type: 'number', message: '请输入数字', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getShops()
  },
  methods: {
    getCategory() {
      //      getShops().then(response => {
      //        this.shopsOptions = response.data
      //      })
    },
    radioChange() {
      console.log(this.goodsDto.goodsMainQuery.isRecommend)
    },
    getList() {
      this.listLoading = true
      fetchDetailList(this.listQuery).then(response => {
        this.list = response.data.results
        this.total = response.data.totalRecord

        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    getShops() {
      getShops().then(response => {
        this.shopsOptions = response.data
      })
    },
    handleEditDialogClose() {
      this.resetGoodsDto()
      this.$refs.uploadBriefPic.clearFiles()
      this.$refs.uploadPrimaryPic.clearFiles()
      this.$refs.uploadListPic.clearFiles()
    },
    handlePrimaryPicClick(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleUploadBriefPicSuccess(response, file, fileList) {
      this.goodsDto.goodsMainQuery.briefPicUrl = response.data
    },
    handleUploadPrimaryPicSuccess(response, file, fileList) {
      this.goodsDto.goodsMainQuery.primaryPicUrls =
        fileList.map(item => item.response.data).join()
    },
    handleRemovePrimaryPic(file, fileList) {
      this.goodsDto.goodsMainQuery.primaryPicUrls =
        fileList.map(item => item.response.data).join()
    },
    handleUploadListPicSuccess(response, file, fileList) {
      this.goodsDto.goodsMainQuery.listPicUrls =
        fileList.map(item => item.response.data).join()
    },
    handleRemoveListPic(file, fileList) {
      this.goodsDto.goodsMainQuery.listPicUrls =
        fileList.map(item => item.response.data).join()
    },
    removeAttribute(item) {
      var index = this.goodsDto.goodsAttributeList.indexOf(item)
      if (index !== -1) {
        this.goodsDto.goodsAttributeList.splice(index, 1)
      }
    },
    addAttribute() {
      this.goodsDto.goodsAttributeList.push({
        name: '',
        value: '',
        key: Date.now()
      })
    },
    removeIssue(item) {
      var index = this.goodsDto.goodsIssueList.indexOf(item)
      if (index !== -1) {
        this.goodsDto.goodsIssueList.splice(index, 1)
      }
    },
    addIssue() {
      this.goodsDto.goodsIssueList.push({
        name: '',
        value: '',
        key: Date.now()
      })
    },
    handleCheckAllShopChange(val) {
      console.log(val)
      console.log(this.goodsDto.shopList)
      console.log(this.shopsOptions.map(item => item.id))
      this.goodsDto.shopList = val ? this.shopsOptions.map(item => item.id) : []
      this.isIndeterminateShop = false
      console.log(this.goodsDto.shopList)
      this.initShopUsers(this.goodsDto.shopList)
    },
    handleCheckedShopsChange(value) {
      console.log(value)
      console.log(this.shopUsersOptions)
      const checkedCount = value.length
      console.log(checkedCount)
      this.checkAllShop = checkedCount === this.shopsOptions.length
      console.log(this.checkAllShop)
      this.isIndeterminateShop = checkedCount > 0 && checkedCount < this.shopsOptions.length
      console.log(this.isIndeterminateShop)
      this.initShopUsers(this.goodsDto.shopList)
    },
    initShopUsers(shops) {
      shops.forEach(shop => {
        getShopUsers(shop).then(response => {
          this.shopUsersOptions.push({
            shopUsers: response.data,
            key: Date.now()
          })
          this.checkAllMerchantUsers.push(
            false
          )
        })
      })
    },
    handleFilter() {
      this.listQuery.pageIndex = 1
      this.getList()
    },
    handlePictureClick(row) {
      this.dialogImageUrl = row.briefPicUrl
      this.dialogVisible = true
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
    resetGoodsDto() {
      this.goodsDto = {
        goodsAttributeList: [
          {
            name: '',
            value: ''
          }
        ],
        goodsIssueList: [
          {
            answer: '',
            question: ''
          }
        ],
        goodsMainQuery: {
          briefPicUrl: '',
          categoryId: undefined,
          counterPrice: '',
          goodsBrief: '',
          goodsDesc: '',
          isHot: true,
          isNew: true,
          isRecommend: true,
          keywords: '',
          listPicUrls: '',
          marketPrice: '',
          name: '',
          onSale: true,
          primaryPicUrls: '',
          sellVolume: undefined,
          sortOrder: undefined
        },
        merchantUserList: [],
        shopList: []
      }
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
