/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/shops',
  component: Layout,
  // redirect: '/table/complex-table',
  name: 'Shops',
  meta: {
    title: '门店',
    icon: 'table'
    // icon: 'el-icon-goods'
  },
  children: [
    {
      path: 'shop-manage',
      component: () => import('@/views/shops/shops'),
      name: 'shopManage',
      meta: { title: '商铺管理' }
    }
  ]
}
export default tableRouter
