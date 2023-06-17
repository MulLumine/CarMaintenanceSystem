<template>
  <div class="dashboard-container">
    <el-divider />
    <el-descriptions v-loading="loading" title="用户信息" size="default" :column="3" style="margin-left: 50px">
      <el-descriptions-item label="用户名" :span="3">{{ userInfo.username }}</el-descriptions-item>
      <el-descriptions-item label="姓名">{{ userInfo.name }}</el-descriptions-item>
      <el-divider />
      <el-descriptions-item label="手机号" :span="2">{{ userInfo.phone }}</el-descriptions-item>
      <el-descriptions-item label="会员等级">
        <el-tag size="small">{{ userInfo.stringGrade }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="权限">
        <el-tag size="small">{{ userInfo.stringMarker }}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <el-divider />
    <el-row style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);border-radius: 30px; margin: 20px auto;width: 98%">
      <el-timeline style="top: 20px;margin-top: 20px; width: 90%">
        <el-timeline-item timestamp="2018/4/12" placement="top">
          <el-card>
            <h4>更新 Github 模板</h4>
            <p>王小虎 提交于 2018/4/12 20:46</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2018/4/3" placement="top">
          <el-card>
            <h4>更新 Github 模板</h4>
            <p>王小虎 提交于 2018/4/3 20:46</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2018/4/2" placement="top">
          <el-card>
            <h4>更新 Github 模板</h4>
            <p>王小虎 提交于 2018/4/2 20:46</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getMoreInfo } from '@/api/user'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  data() {
    return {
      userInfo: {},
      loading: true,
      value: new Date()
    }
  },
  created() {
    this.getUserMoreInfo()
  }, methods: {
    getUserMoreInfo() {
      this.loading = true
      getMoreInfo().then(res => {
        this.userInfo = res.data
        // eslint-disable-next-line
        if (this.userInfo.grade == '2') { this.userInfo.stringGrade = '超级会员' }
        // eslint-disable-next-line eqeqeq
        else if (this.userInfo.grade == '1') { this.userInfo.stringGrade = '普通会员' } else this.userInfo.stringGrade = '普通用户'
        // eslint-disable-next-line
        if (this.userInfo.marker == 2) { this.userInfo.stringMarker = '超级管理员' } else if (this.userInfo.marker == 1) { this.userInfo.stringMarker = '管理员' } else this.userInfo.stringMarker = '普通用户'
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
