/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/coupon',
  component: Layout,
  // redirect: '/table/complex-table',
  name: 'oupon',
  meta: {
    title: '卡券',
    icon: 'table'
    // icon: 'el-icon-goods'
  },
  children: [
    {
      path: 'coupon-manage',
      component: () => import('@/views/coupon/coupon'),
      name: 'couponManage',
      meta: { title: '优惠券管理' }
    }
  ]
}
export default tableRouter
