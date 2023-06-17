<template>
  <div>
    <el-row v-loading="loading" style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);border-radius: 30px;width: 95%; margin: 25px auto;  background: #fafafa;">
      <div style="margin-bottom: 30px" />

      <el-row v-if="isShow" style="margin: 10px 20px">
        <el-input v-model="searchObj.name" style="width: 200px; margin-left: 10px" placeholder="车主姓名" suffix-icon="el-icon-user" />
        <el-input v-model="searchObj.phone" style="width: 200px; margin-left: 10px" placeholder="车主电话" suffix-icon="el-icon-phone" />

        <el-button style="margin-left: 50px" type="primary" round size="medium" @click="loadInfo">搜索 <i class="el-icon-search" /></el-button>
        <el-button class="ml-5" type="warning" round size="medium" @click="resetObj">重置 <i class="el-icon-refresh" /></el-button>
        <!--        <el-button class="ml-5" type="success" round size="medium" @click="AddNewCar">新增 <i class="el-icon-plus" /></el-button>-->
      </el-row>

      <el-row v-if="isShow" style="margin: 10px 60px 10px 20px;">
        <el-input v-model="searchObj.type" style="width: 200px; margin-left: 10px" placeholder="车辆型号" suffix-icon="el-icon-truck" />
        <el-input v-model="searchObj.situation" style="width: 200px;margin-left: 10px" placeholder="车辆情况" suffix-icon="el-icon-outline" class="ml-5" />
        <el-select v-model="searchObj.grade" placeholder="会员等级" class="ml-5" style="width: 100px;margin-left: 10px" clearable suffix-icon="el-icon-question">
          <el-option
            v-for="item in graderlist"
            :key="item"
            :label="item==2?'超级会员':item==1?'普通会员':'普通用户'"
            :value="item"
          />
        </el-select>
        <span style="margin-left:15px ">
          <span style="font-size: 14px;color: #a6a6a9">订单状态:</span>
          <el-switch
            v-model="searchObj.toolstatus"
            style="margin-left: 10px"
            active-color="#13ce66"
            inactive-color="#C6C6C6"
            active-text="已完成"
          /></span>
      </el-row>

      <el-table v-loading="loading" :data="tableData" border stripe style="margin: 0 auto;width: 95%" size="medium">
        <el-table-column prop="id" label="订单ID" width="80" />
        <el-table-column prop="name" label="车主" width="80" />
        <el-table-column prop="stringGrade" label="会员等级" width="80" />
        <el-table-column prop="type" label="车辆型号" width="100" />
        <el-table-column prop="situation" label="订单情况" width="350" />
        <el-table-column prop="stringstatus" label="订单状态" />
        <el-table-column prop="phone" label="车主电话" width="120" />
        <el-table-column label="操作" width="300" align="center" fixed="right">
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope" >
            <el-button round type="primary" @click="GetMoreInfo(scope.row.componentList);">零件详细 <i class="el-icon-share" /></el-button>
            <el-button round type="success" @click="handleEdit(scope.row);">编辑 <i class="el-icon-edit" /></el-button>
            <el-button slot="reference" round type="danger" @click="openDelWindowTodelOrder(scope.row.id)">删除 <i class="el-icon-close" /></el-button>
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
      <el-dialog title="订单所用零件" :visible.sync="moreInfodialog" width="30%">
        <el-row v-for="i in moreConInfo" :key="i" :value="i" style="margin-bottom: 10px;margin-left: 20px"><strong>零件描述</strong>   {{ i.description }}<strong style="margin-left: 50px">零件售价</strong>   {{ i.outprice }}</el-row>
      </el-dialog>
      <!--   信息更新表单   -->
      <el-dialog title="订单信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form ref="ruleForm" :model="form" label-width="60px" size="small" :rules="rules">
          <el-form-item label="车辆情况" label-width="78px" prop="situation">
            <el-input v-model="form.situation" autocomplete="off" />
          </el-form-item>
          <el-form-item label="订单价格" label-width="78px" prop="price">
            <el-input v-model="form.price" autocomplete="off" />
          </el-form-item>
          <el-form-item label="订单状态" label-width="78px">
            <el-radio-group v-model="form.status">
              <el-radio :label="0">未完成</el-radio>
              <el-radio :label="1">已完成</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="零件选择" label-width="78px" prop="comListObj">
            <el-checkbox-group v-model="comListObj">
              <template v-for="item in componentList">
                <el-checkbox :key="item.id" :label="item.id">{{ item.description }}</el-checkbox>
              </template>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveOrderInfo('ruleForm')">确 定</el-button>
        </div>

        <!--      </el-dialog><el-dialog title="车辆信息" :visible.sync="addDialogForm" width="30%">
        <el-form :model="addForm" label-width="60px" size="small">
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
      </el-dialog>-->
      </el-dialog></el-row>
  </div>
</template>

<script>

import { delOrder, GetOrderInfo, updateOrderInfo } from '@/api/order'
import { Message } from 'element-ui'
import { getAll } from '@/api/components'
// eslint-disable-next-line no-unused-vars
import router from '@/router'

export default {
  name: 'OrderView',
  data() {
    // eslint-disable-next-line no-unused-vars
    const checkInt = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('数字不能为空！'))
      }
      setTimeout(() => {
        // eslint-disable-next-line no-unused-vars
        if (!Number.isInteger(parseInt(value))) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }, 1000)
    }
    return {
      test: [],
      comListObj: [],
      componentList: [],
      addDialogForm: false,
      addForm: {},
      dialogFormVisible: false,
      form: {},
      moreConInfo: [],
      moreInfodialog: false,
      isShow: true,
      searchObj: {},
      graderlist: [0, 1, 2],
      tableData: [],
      toolTableData: [],
      loading: true,
      pageNum: 1,
      pageSize: 8,
      total: 0,
      rules: {
        price: { required: true, trigger: 'blur', validator: checkInt },
        situation: { required: true, trigger: 'blur', message: '车辆情况不能为空' }
      }
    }
  },
  created() {
    getAll().then(res => {
      Object.assign(this.componentList, res.data)
      this.loadInfo()
    })
  },
  methods: {
    async getComList() {
      this.componentList = []
      await getAll().then(res => {
        this.componentList = res.data
      })
    },
    resetObj() {
      this.searchObj = {}
    },
    loadInfo() {
      this.loading = true
      this.tableData = []
      if (this.searchObj.toolstatus === true) { this.searchObj.status = 1 } else this.searchObj.status = 0
      GetOrderInfo(this.pageNum, this.pageSize, this.searchObj).then(res => {
        this.tableData = res.data.records
        this.tableData.forEach(i => {
          // eslint-disable-next-line eqeqeq
          if (i.grade == 2) { i.stringGrade = '超级会员' } else if (i.grade == 1) { i.stringGrade = '普通会员' } else i.stringGrade = '普通用户'
          if (i.status === 1) {
            i.stringstatus = '已完成'
          } else i.stringstatus = '未完成'
        })
        this.total = res.data.total
        this.loading = false
      })
    },
    handleSizeChange(pageSize) {
      this.loading = true
      this.pageSize = pageSize
      this.loadInfo()
    },
    handleCurrentChange(pageNum) {
      this.loading = true
      this.pageNum = pageNum
      this.loadInfo()
    },
    GetMoreInfo(val) {
      this.moreConInfo = []
      this.moreConInfo = val
      this.moreInfodialog = true
    },
    handleEdit(val) {
      this.form = []
      this.comListObj = []
      this.form = val
      const newList = []
      for (const i in this.form.componentList) {
        newList.push(this.form.componentList[i].id)
      }
      this.comListObj = newList
      this.dialogFormVisible = true
    },
    saveOrderInfo(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // eslint-disable-next-line
          let subForm = {}
          subForm.id = this.form.id
          subForm.price = this.form.price
          subForm.situation = this.form.situation
          subForm.status = this.form.status
          updateOrderInfo(subForm, this.comListObj.toLocaleString()).then(() => {
            this.loading = true
            this.loadInfo()
            Message({
              message: '操作成功！',
              type: 'success',
              showClose: true
            })
            this.dialogFormVisible = false
          }).catch(error => {
            console.log(error)
          })
        } else {
          Message({
            message: '信息提交有误',
            type: 'error',
            showClose: true
          })
          return false
        }
      })
    },
    openDelWindowTodelOrder(id) {
      this.$confirm('此操作将永久删除该订单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        delOrder(id).then(() => {
          this.loadInfo()
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
    }
  }
}
</script>

<style scoped>

</style>
