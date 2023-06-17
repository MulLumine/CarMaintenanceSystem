<template>
  <div>
    <el-row v-loading="loading" style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);border-radius: 30px;width: 95%; margin: 25px auto;  background: #fafafa;">
      <div style="margin-bottom: 30px" />

      <el-row v-if="isShow" style="margin: 10px 20px">
        <el-input v-model="searchObj.name" style="width: 200px; margin-left: 10px" placeholder="车主姓名" suffix-icon="el-icon-user" />
        <el-input v-model="searchObj.phone" style="width: 200px; margin-left: 5px" placeholder="车主电话" suffix-icon="el-icon-phone" />
        <el-button style="margin-left: 50px" type="primary" round size="medium" @click="loadCarsInfo">搜索 <i class="el-icon-search" /></el-button>
        <el-button class="ml-5" type="warning" round size="medium" @click="resetObj">重置 <i class="el-icon-refresh" /></el-button>
        <el-button class="ml-5" type="success" round size="medium" @click="AddNewCar">新增 <i class="el-icon-plus" /></el-button>
      </el-row>

      <el-row v-if="isShow" style="margin: 10px 60px 10px 20px;">
        <el-input v-model="searchObj.type" style="width: 200px; margin-left: 10px" placeholder="车辆型号" suffix-icon="el-icon-eleme" />
        <el-input v-model="searchObj.description" style="width: 200px" placeholder="车辆描述" suffix-icon="el-icon-document" class="ml-5" />
        <el-input v-model="searchObj.code" style="width: 200px" placeholder="车牌号" suffix-icon="el-icon-paperclip" class="ml-5" />
        <el-input v-model="searchObj.color" style="width: 200px" placeholder="颜色" suffix-icon="el-icon-view" class="ml-5" />
      </el-row>

      <el-table v-loading="loading" :data="tableData" border stripe style="margin: 0 auto;width: 95%" size="medium">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="type" label="车辆型号" width="140" />
        <el-table-column prop="code" label="车牌号" width="120" />
        <el-table-column prop="color" label="颜色" width="80" />
        <el-table-column prop="name" label="车主" />
        <el-table-column prop="phone" label="车主电话" />
        <el-table-column prop="description" label="车辆描述" width="350" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope" >
            <el-button round type="success" @click="handleEdit(scope.row);">编辑 <i class="el-icon-edit" /></el-button>

            <el-button v-if="ii" round slot="reference" type="danger" @click="openCarDelWindow(scope.row.id)">删除 <i class="el-icon-close" /></el-button>

          </template>
        </el-table-column>
      </el-table>
      <div style="padding: 10px 0">
        <el-pagination
          style="margin-left: 30%"
          :current-page="pageNum"
          :page-sizes="[4, 8, 10, 15]"
          :page-size="pageSize"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!--   信息更新表单   -->
      <el-dialog title="车辆信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form" label-width="60px" size="small">
          <el-form-item label="车辆型号" label-width="75px">
            <el-input v-model="form.type" autocomplete="off" />
          </el-form-item>
          <el-form-item label="车牌号" label-width="75px">
            <el-input v-model="form.code" autocomplete="off" />
          </el-form-item>
          <el-form-item label="车辆颜色" label-width="75px">
            <el-input v-model="form.color" autocomplete="off" />
          </el-form-item>
          <el-form-item label="车辆描述" label-width="75px">
            <el-input v-model="form.description" autocomplete="off" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="CarSave">确 定</el-button>
        </div>

      </el-dialog><el-dialog title="车辆信息" :visible.sync="addDialogForm" width="30%">
        <el-form :model="addForm" label-width="60px" size="small" :rules="rules">
          <el-form-item label="车辆型号" label-width="78px" prop="type">
            <el-input v-model="addForm.type" autocomplete="off" />
          </el-form-item>
          <el-form-item label="车  牌  号" label-width="78px" prop="code">
            <el-input v-model="addForm.code" autocomplete="off" />
          </el-form-item>
          <el-form-item label="车辆颜色" label-width="78px" prop="color">
            <el-input v-model="addForm.color" autocomplete="off" />
          </el-form-item>
          <el-form-item label="车辆描述" label-width="78px" prop="description">
            <el-input v-model="addForm.description" autocomplete="off" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="AddCar">确 定</el-button>
        </div>
      </el-dialog>
    </el-row>
  </div>
</template>

<script>// eslint-disable-next-line
import { changeUserRole, delUser, getInfoByToken, getUserInfo, updateUserInfo } from '@/api/user' // eslint-disable-next-line
import { Message } from 'element-ui'
// eslint-disable-next-line no-unused-vars
import router from '@/router'
// eslint-disable-next-line no-unused-vars
import { addCar, deleteCar, getCarInfoByPage, updateCarsInfo } from '@/api/car'
// eslint-disable-next-line no-unused-vars
import { error } from 'autoprefixer/lib/utils'

export default {
  name: 'CarView',
  data() {
    return {
      ii: true,
      addForm: {},
      addDialogForm: false,
      isShow: false,
      pageNum: 1,
      pageSize: 8,
      total: 0,
      loading: true,
      searchObj: {},
      tableData: [],
      dialogFormVisible: false,
      form: {},
      rules: {
        type: { required: true, trigger: 'blur', message: '车辆型号不能为空！' },
        code: { required: true, trigger: 'blur', message: '车牌号不能为空！' }
      }
    }
  },
  created() {
    this.getTokeninfo()
    this.loading = false
    this.loadCarsInfo()
  },
  methods: {
    resetObj() {
      this.searchObj = {}
    },
    loadCarsInfo() {
      this.tableData = []
      this.loading = true
      getCarInfoByPage(this.searchObj, this.pageNum, this.pageSize).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading = false
      }).catch(() => {
        // eslint-disable-next-line eqeqeq
      })
    },
    getTokeninfo() {
      getInfoByToken().then(res => {
        if (res.data.marker !== 2) { this.isShow = false } else this.isShow = true
        if (res.data.marker === 1) this.ii = false
      })
    },
    handleCurrentChange(pageNum) {
      this.loading = true
      this.pageNum = pageNum
      this.loadCarsInfo()
    },
    handleSizeChange(pageSize) {
      this.loading = true
      this.pageSize = pageSize
      this.loadCarsInfo()
    },
    handleEdit(val) {
      this.form = {}
      this.form = val
      this.dialogFormVisible = true
    },
    CarSave() {
      this.loading = true
      updateCarsInfo(this.form).then(() => {
        Message({
          message: '更新成功',
          type: 'success',
          showClose: true
        })
        this.dialogFormVisible = false
        this.loadCarsInfo()
      })
    },
    openCarDelWindow(id) {
      this.$confirm('此操作将永久删除该车辆, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const carId = id
        deleteCar(carId).then(() => {
          this.loading = true
          this.loadCarsInfo()
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    AddNewCar() {
      this.addDialogForm = true
    },
    AddCar() {
      this.loading = true
      addCar(this.addForm).then(() => {
        this.loadCarsInfo()
        Message({
          message: '操作成功',
          type: 'success',
          showClose: true
        })
        this.addDialogForm = false
      })
    }
  }
}
</script>

<style scoped>
.headerBg {
  background: #eee!important;
}
.ml-5 {
  margin-left: 8px;
}
</style>
