/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/members',
  component: Layout,
  // redirect: '/table/complex-table',
  name: 'Members',
  meta: {
    title: '会员',
    icon: 'table'
    // icon: 'el-icon-goods'
  },
  children: [
    {
      path: 'members-manage',
      component: () => import('@/views/members/members'),
      name: 'membersManage',
      meta: { title: '会员管理' }
    },
    {
      path: 'members-welfare',
      component: () => import('@/views/members/welfare'),
      name: 'membersWelfare',
      meta: { title: '会员福利' }
    }
  ]
}
export default tableRouter
