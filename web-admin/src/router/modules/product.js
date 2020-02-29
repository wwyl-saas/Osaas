/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/product',
  component: Layout,
  // redirect: '/table/complex-table',
  name: 'Product',
  meta: {
    title: '商品管理',
    icon: 'table'
    // icon: 'el-icon-goods'
  },
  children: [
    {
      path: 'product-classify',
      component: () => import('@/views/product/product-classify'),
      name: 'ProductClassify',
      meta: { title: '分类管理' }
    },
    {
      path: 'product-detail',
      component: () => import('@/views/product/product-detail'),
      name: 'ProductDetail',
      meta: { title: '商品管理' }
    }
  ]
}
export default tableRouter
