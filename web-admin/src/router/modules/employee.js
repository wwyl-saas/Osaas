/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/employee',
  component: Layout,
  // redirect: '/table/complex-table',
  name: 'Employee',
  meta: {
    title: '用户',
    icon: 'table'
    // icon: 'el-icon-goods'
  },
  children: [
    {
      path: 'employee-manage',
      component: () => import('@/views/employee/employee'),
      name: 'employeeManage',
      meta: { title: '员工管理' }
    },
    {
      path: 'employee-post',
      component: () => import('@/views/employee/post'),
      name: 'employeePost',
      meta: { title: '职称管理' }
    }
  ]
}
export default tableRouter
