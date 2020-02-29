/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/order',
  component: Layout,
  // redirect: '/table/complex-table',
  name: 'Order',
  meta: {
    title: '订单',
    icon: 'table'
    // icon: 'el-icon-goods'
  },
  children: [
    {
      path: 'order-manage',
      component: () => import('@/views/order/order'),
      name: 'orderManage',
      meta: { title: '订单管理' }
    }
  ]
}
export default tableRouter
