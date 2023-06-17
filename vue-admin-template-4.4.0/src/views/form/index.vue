<template>
  <div class="app-container">
    <el-steps :space="400" :active="active" finish-status="success" style="margin-left: 270px">
      <el-step title="选择车辆" />
      <el-step title="选择零件" />
      <el-step title="提交" />
    </el-steps>
    <el-row style="margin-top: 30px;border-radius: 30px;box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)">
      <el-row ref="form" :model="form" label-width="120px" style="margin: 20px auto; width: 95%">
        <el-table v-if="active==0" v-loading="loading" :data="tableData" border stripe style="margin: 0 auto;width: 90%" size="medium">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="type" label="车辆型号" width="140" />
          <el-table-column prop="code" label="车牌号" width="120" />
          <el-table-column prop="color" label="颜色" width="80" />
          <el-table-column prop="description" label="车辆描述" />
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <!-- eslint-disable-next-line -->
            <template slot-scope="scope" >
              <el-button size="medium" round type="success" @click="selectThis(scope.row);">选择这辆车？ <i class="el-icon-check" /></el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-form v-if="active==1" :model="form" label-width="60px" size="small" style="margin-left: 15%; margin-top: 2%">
          <el-form-item label="车辆情况" label-width="75px">
            <el-input
              v-model="form.situation"
              autocomplete="off"
              style="width: 70%"
              type="textarea"
              :autosize="{ minRows: 4, maxRows: 6}"
              placeholder="请输入内容"
            />
          </el-form-item>
          <el-form-item label="零件选择" label-width="75px">
            <el-checkbox-group v-model="comListObj">
              <template v-for="item in componentList">
                <el-checkbox :key="item.id" :label="item.id">{{ item.description }}</el-checkbox>
              </template>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <div v-if="active==1" style="margin-left: 40%">
          <el-button size="medium" @click="active = 0">返  回</el-button>
          <el-button size="medium" type="primary" @click="saveOrderInfo">提交</el-button>
        </div>
        <el-result v-if="active==3" icon="success" title="成功！" sub-title="已成功新增订单！">
          <template slot="extra">
            <el-button type="primary" size="medium" @click="back">返回</el-button>
          </template>
        </el-result>
      </el-row>
    </el-row>
  </div>
</template>

<script>
import { getOwnCar } from '@/api/car'
import { getAll } from '@/api/components'
import { addNewOrder } from '@/api/order'

export default {
  data() {
    return {
      comListObj: [],
      componentList: [],
      active: 0,
      tableData: [],
      loading: true,
      form: {},
      carInfo: {}
    }
  },
  created() {
    this.getUserCar()
    getAll().then(res => {
      Object.assign(this.componentList, res.data)
    })
  },
  methods: {
    getUserCar() {
      getOwnCar().then(res => {
        this.tableData = res.data
        this.loading = false
      })
    },
    selectThis(val) {
      this.carInfo = {}
      this.carInfo = val
      this.active = 1
    },
    async  saveOrderInfo() {
      this.carInfo.situation = null
      this.carInfo.situation = this.form.situation
      const order = {}
      order.situation = this.carInfo.situation
      order.carId = this.carInfo.id
      await addNewOrder(order, this.comListObj.toLocaleString())
      this.active = 3
    },
    back() {
      this.active = 0
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

