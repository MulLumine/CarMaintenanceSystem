<template>
  <div>
    <el-row v-loading="loading" style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);border-radius: 30px;width: 95%; margin: 25px auto;  background: #fafafa;">
      <div style="margin-bottom: 30px" />

      <el-row v-if="isShow" style="margin: 10px 20px">
        <el-input v-model="searchObj.number" style="width: 200px; margin-left: 10px" placeholder="零件数量(多少以下)" suffix-icon="el-icon-user" />
        <el-input v-model="searchObj.description" style="width: 200px" placeholder="零件描述" suffix-icon="el-icon-document" class="ml-5" />

        <el-button style="margin-left: 50px" type="primary" round size="medium" @click="loadComInfo">搜索 <i class="el-icon-search" /></el-button>
        <el-button class="ml-5" type="warning" round size="medium" @click="resetObj">重置 <i class="el-icon-refresh" /></el-button>
        <el-button class="ml-5" type="success" round size="medium" @click="addObj">新增 <i class="el-icon-plus" /></el-button>
      </el-row>

      <el-row v-if="isShow" style="margin: 10px 60px 10px 20px;">
        <span class="searchFontSet"> 进价范围:
          <el-input-number v-model="searchObj.inprice" size="small" style="margin-right: 5px" />
          至
          <el-input-number v-model="searchObj.maxInprice" size="small" style="margin-left: 5px" />
        </span>
        <span class="searchFontSet" style="margin-left: 20px;"> 售价范围:
          <el-input-number v-model="searchObj.outprice" size="small" style="margin-right: 5px" />
          至
          <el-input-number v-model="searchObj.maxOutprice" size="small" style="margin-left: 5px" />
        </span>
      </el-row>

      <el-table v-loading="loading" :data="tableData" border stripe style="margin: 0 auto;width: 95%" size="medium">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="number" label="零件存量" width="140" />
        <el-table-column prop="inprice" label="零件进价" width="120" />
        <el-table-column prop="outprice" label="零件售价" width="120" />
        <el-table-column prop="description" label="零件描述" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope" >
            <el-button round type="success" @click="handleEdit(scope.row);">编辑 <i class="el-icon-edit" /></el-button>
            <el-button slot="reference" round type="danger" @click="getDelWindow(scope.row.id)">删除 <i class="el-icon-close" /></el-button>

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
      <el-dialog title="零件信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form ref="ruleForm" :model="form" :rules="rules" label-width="60px" size="small">
          <el-form-item label="现存数量" label-width="78px" prop="number">
            <el-input v-model="form.number" autocomplete="off" />
          </el-form-item>
          <el-form-item label="零件进价" label-width="78px" prop="inprice">
            <el-input v-model="form.inprice" autocomplete="off" />
          </el-form-item>
          <el-form-item label="零件售价" label-width="78px" prop="outprice">
            <el-input v-model="form.outprice" autocomplete="off" />
          </el-form-item>
          <el-form-item label="零件描述" label-width="78px" prop="description">
            <el-input v-model="form.description" autocomplete="off" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmForm('ruleForm')">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="零件信息" :visible.sync="otherDialog" width="30%">
        <el-form ref="TheRuleForm" :model="addform" :rules="theRules" label-width="60px" size="small">
          <el-form-item label="现存数量" label-width="78px" prop="number">
            <el-input v-model="addform.number" autocomplete="off" />
          </el-form-item>
          <el-form-item label="零件进价" label-width="78px" prop="inprice">
            <el-input v-model="addform.inprice" autocomplete="off" />
          </el-form-item>
          <el-form-item label="零件售价" label-width="78px" prop="outprice">
            <el-input v-model="addform.outprice" autocomplete="off" />
          </el-form-item>
          <el-form-item label="零件描述" label-width="78px" prop="description">
            <el-input v-model="addform.description" autocomplete="off" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="otherDialog = false">取 消</el-button>
          <el-button type="primary" @click="insertNewObj('TheRuleForm')">确 定</el-button>
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
import { deleteCar, getCarInfoByPage, updateCarsInfo } from '@/api/car'
// eslint-disable-next-line no-unused-vars
import { error } from 'autoprefixer/lib/utils'
import { addComponents, deleteComponents, getComponentsInfoByPage, updateComponents } from '@/api/components'
import { integer } from 'mockjs/src/mock/random/basic'

export default {
  name: 'ComponentView',
  data() {
    const checkInt = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('数字不能为空！'))
      }
      setTimeout(() => {
        // eslint-disable-next-line no-unused-vars
        var integer1 = integer(value)
        if (!Number.isInteger(integer1)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }, 1000)
    }
    return {
      otherDialog: false,
      isShow: true,
      pageNum: 1,
      pageSize: 8,
      total: 0,
      loading: true,
      searchObj: {},
      tableData: [],
      dialogFormVisible: false,
      form: {},
      addform: {
        number: 0,
        inprice: 0,
        outprice: 0,
        description: ''
      },
      rules: {
        number: { required: true, trigger: 'blur', validator: checkInt },
        inprice: { required: true, trigger: 'blur', validator: checkInt },
        outprice: { required: true, trigger: 'blur', validator: checkInt }
      },
      theRules: {
        number: { required: true, trigger: 'blur', validator: checkInt },
        inprice: { required: true, trigger: 'blur', validator: checkInt },
        outprice: { required: true, trigger: 'blur', validator: checkInt },
        description: { required: true, trigger: 'blur' }
      }
    }
  },
  created() {
    this.loadComInfo()
  },
  methods: {
    loadComInfo() {
      this.loading = true
      getComponentsInfoByPage(this.searchObj, this.pageNum, this.pageSize).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading = false
      })
    },
    handleSizeChange(pageSize) {
      this.loading = true
      this.pageSize = pageSize
      this.loadComInfo()
    },
    handleCurrentChange(pageNum) {
      this.loading = true
      this.pageNum = pageNum
      this.loadComInfo()
    },
    resetObj() {
      this.searchObj = {}
    },
    handleEdit(val) {
      this.form = {}
      this.dialogFormVisible = true
      this.form = val
    },
    confirmForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          updateComponents(this.form).then(() => {
            this.loading = true
            this.loadComInfo()
            this.dialogFormVisible = false
            Message({
              message: '更新成功！',
              type: 'success',
              showClose: true
            })
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
    getDelWindow(id) {
      this.$confirm('此操作将永久删除该零件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteComponents(id)
        this.loadComInfo()
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    addObj() {
      this.otherDialog = true
    },
    insertNewObj(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          addComponents(this.addform).then(() => {
            this.loading = true
            this.loadComInfo()
            this.otherDialog = false
            Message({
              message: '新增成功！',
              type: 'success',
              showClose: true
            })
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
.searchFontSet {
  font-size: 12px;
  color: #b4b4b4;
  margin-left: 11px;
}
</style>
