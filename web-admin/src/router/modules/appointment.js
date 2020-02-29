/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/appointment',
  component: Layout,
  // redirect: '/table/complex-table',
  name: 'Appointment',
  meta: {
    title: '预约',
    icon: 'table'
    // icon: 'el-icon-goods'
  },
  children: [
    {
      path: 'appointment-manage',
      component: () => import('@/views/appointment/appointment'),
      name: 'appointmentManage',
      meta: { title: '预约管理' }
    }
  ]
}
export default tableRouter
