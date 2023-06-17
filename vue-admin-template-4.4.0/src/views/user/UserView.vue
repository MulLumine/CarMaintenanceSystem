<template>
  <div>
    <el-row v-loading="loading" style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);border-radius: 30px;width: 95%; margin: 25px auto;  background: #fafafa;" name="el-zoom-in-bottom">
      <div style="margin-bottom: 30px" />

      <div style="margin: 10px auto;width: 97%">
        <el-input v-model="user.name" style="width: 200px; margin-left: 10px" placeholder="请输入名称" suffix-icon="el-icon-user" />
        <el-input v-model="user.phone" style="width: 200px" placeholder="请输入电话" suffix-icon="el-icon-phone" class="ml-5" />
        <el-select v-model="user.gender" placeholder="性别" class="ml-5" style="width: 80px" clearable icon="el-icon-question">
          <el-option
            v-for="item in genderlist"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        <el-select v-model="toolgrade" placeholder="会员等级" class="ml-5" style="width: 100px" clearable suffix-icon="el-icon-question">
          <el-option
            v-for="item in graderlist"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        <el-select v-if="this.nowUserMarker===2" v-model="toolmarker" placeholder="身份(默认搜用户)" class="ml-5" style="width: 150px" suffix-icon="el-icon-collection">
          <el-option
            v-for="item in markerlist"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        <el-button style="margin-left: 50px" type="primary" round size="medium" @click="loadUserInfo">搜索 <i class="el-icon-search" /></el-button>
        <el-button class="ml-5" type="warning" round size="medium" @click="resetObj">重置 <i class="el-icon-refresh" /></el-button>
      </div>

      <!--        <div style="margin: 10px 0">
      <el-popconfirm
        class="mr-5"
        confirm-button-text="确认删除"
        cancel-button-text="我再想想"
        icon="el-icon-info"
        icon-color="red"
        title="您确定删除吗？"
        @confirm="delByIds"
      >
        <el-button slot="reference" type="danger" class="mr-5">批量删除 <el-icon><Close /></el-icon></el-button>
      </el-popconfirm>

      <el-button type="primary" style="margin-left: 10px" @click="Bailan">导入 <i class="el-icon-bottom" /></el-button>
      <el-button type="primary" @click="Bailan">导出 <i class="el-icon-top" /></el-button>
    </div>-->

      <el-table :data="tableData" border stripe style="margin: 0 auto;width: 95%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="name" label="用户姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="grade" label="等级" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="marker" label="权限" />
        <el-table-column label="操作" width="400" align="center" fixed="right">
          <!-- eslint-disable-next-line -->
      <template slot-scope="scope" >
            <el-button round type="success" @click="handleEdit(scope.row);">编辑 <i class="el-icon-edit" /></el-button>

            <el-button slot="reference" round type="danger" @click="openDelWindow(scope.row.id)" v-if="nowUserMarker==2">删除 <i class="el-icon-close" /></el-button>

            <el-button round type="warning" :disabled="disabled" @click="changeRule(scope.row)">权限修改 <i class="el-icon-warning-outline" /></el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="padding: 10px 0">
        <el-pagination
          style="margin-left: 30%"
          :current-page="pageNum"
          :page-sizes="[8, 10, 15]"
          :page-size="pageSize"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      <!--   信息更新表单   -->
      <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form" label-width="60px" size="small">
          <el-form-item label="用户姓名" label-width="75px">
            <el-input v-model="form.name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="用户电话" label-width="75px">
            <el-input v-model="form.phone" autocomplete="off" />
          </el-form-item>
          <el-form-item label="用户性别" label-width="75px">
            <el-input v-model="form.gender" autocomplete="off" />
          </el-form-item>
          <el-form-item label="会员等级" label-width="75px">
            <el-radio-group v-model="form.grade">
              <el-radio :label="0">普通用户</el-radio>
              <el-radio :label="1">普通会员</el-radio>
              <el-radio :label="2">超级会员</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="UserSave">确 定</el-button>
        </div>
      </el-dialog>

      <!--权限更新表单-->
      <el-dialog title="权限修改" :visible.sync="ruledialogFrom" width="500px">
        <el-radio-group v-model="marker" style="margin-left: 30px">
          <el-radio :label="0">普通用户</el-radio>
          <el-radio :label="1">管理员</el-radio>
        </el-radio-group>
        <div slot="footer" class="dialog-footer">
          <el-button @click="ruledialogFrom = false">取 消</el-button>
          <el-button type="primary" @click="updateRule">确 定</el-button>
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

export default {
  name: 'UserView',
  data() {
    return {
      marker: null,
      userinfo: {},
      tableDataForData: null,
      disabled: true,
      nowUserId: null,
      nowUserMarker: null,
      loading: true,
      toolmarker: '',
      // eslint-disable-next-line
      toolgrade: '',
      dialogFormVisible: false,
      ruledialogFrom: false,
      form: {},
      user:
        {
          name: '',
          phone: '',
          gender: '',
          grade: '',
          marker: 0
        },
      genderlist: ['男', '女'],
      graderlist: ['普通用户', '普通会员', '超级会员'],
      markerlist: ['用户', '管理'],
      total: 0,
      pageNum: 1,
      pageSize: 8,
      tableData: []
    }
  },
  created() {
    this.getTokeninfo()
    this.loadUserInfo()
    this.resetObj()
  },
  methods: {
    resetObj() {
      this.user = {
        name: '',
        phone: '',
        gender: '',
        grade: '',
        marker: 0
      }
    },
    loadUserInfo() {
      this.tableData = []
      // eslint-disable-next-line eqeqeq
      if (this.toolmarker == '超管') { this.user.marker = 2 } else if (this.toolmarker == '管理') { this.user.marker = 1 } else this.user.marker = 0
      // eslint-disable-next-line eqeqeq
      if (this.toolgrade == '超级会员') { this.user.grade = 2 } else if (this.toolgrade == '普通会员') { this.user.grade = 1 } else if (this.toolgrade == '') {
        this.user.grade = ''
      } else this.user.grade = 0
      getUserInfo(this.pageNum, this.pageSize, this.user).then(res => {
        /* Object.assign(this.tableData, res.data.records) */
        this.tableDataForData = res.data.records
        this.tableData = res.data.records
        this.total = res.data.total
        this.tableData.forEach(item => {
          // eslint-disable-next-line
          if (item.marker == 2) {
            item.marker = '超级管理员'
            // eslint-disable-next-line
          } else if (item.marker == 1) {
            item.marker = '管理员'
          } else item.marker = '普通用户'

          // eslint-disable-next-line eqeqeq
          if (item.grade == 2) {
            item.grade = '超级会员'
            // eslint-disable-next-line
          } else if (item.grade == 1) {
            item.grade = '普通会员'
          } else item.grade = '普通用户'
        })
      })
      this.loading = false
    },
    getTokeninfo() {
      getInfoByToken().then(res => {
        this.nowUserId = res.data.id
        this.nowUserMarker = res.data.marker
        if (res.data.marker !== 2) { this.disabled = true } else this.disabled = false
      })
    },
    handleSizeChange(pageSize) {
      this.loading = true
      this.pageSize = pageSize
      this.loadUserInfo()
    },
    handleCurrentChange(pageNum) {
      this.loading = true
      this.pageNum = pageNum
      this.loadUserInfo()
    },
    del(id) {
      delUser(id).then(() => {
        this.loading = true
        this.loadUserInfo()
      })
    },
    openDelWindow(id) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.del(id)
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
    handleEdit(val) {
      console.log(val)
      const toolform = val
      if (toolform.grade === '超级会员') { toolform.grade = 2 } else if (toolform.grade === '普通会员') { toolform.grade = 1 } else toolform.grade = 0
      if (toolform.marker === '超级管理员') { toolform.marker = 2 } else if (toolform.marker === '管理员') { toolform.marker = 1 } else toolform.marker = 0
      this.form = toolform
      this.dialogFormVisible = true
      this.loadUserInfo()
    },
    UserSave() {
      this.loading = true
      updateUserInfo(this.form).then(() => {
        this.loadUserInfo()
        this.dialogFormVisible = false
        Message({
          message: '修改成功！',
          type: 'success',
          showClose: true
        })
      })
    },
    changeRule(user) {
      this.userinfo = user
      if (user.marker === '超级管理员') { this.userinfo.marker = 2 }
      if (user.marker === '管理员') { this.userinfo.marker = 1 } else this.userinfo.marker = 0
      this.marker = this.userinfo.marker
      this.ruledialogFrom = true
    },
    updateRule() {
      this.loading = true
      // eslint-disable-next-line eqeqeq
      if (this.userinfo.grade == '超级会员') { this.userinfo.grade = 2 } else if (this.userinfo.grade == '普通会员') { this.userinfo.grade = 1 } else this.userinfo.grade = 0
      this.userinfo.marker = this.marker
      changeUserRole(this.userinfo).then(() => {
        this.loadUserInfo()
        this.ruledialogFrom = false
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
