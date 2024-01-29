import Vue from "vue";
import Router from "vue-router";
// NOTE: 这个文件直接改就可以改侧边的menu
Vue.use(Router);

/* Layout */
import Layout from "@/layout";

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */

// NOTE: 这个文件直接改就可以改侧边的menu
// TODO: breadcrumb这块还有问题,有时间研究下
export const constantRoutes = [
  {
    path: "/login",
    component: () => import("@/views/login/index"),
    hidden: true,
  },

  {
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true,
  },

  {
    path: "/",
    component: Layout,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        name: "Dashboard",
        component: () => import("@/views/dashboard/index"),
        meta: { title: "主页", icon: "el-icon-s-home" },
      },
    ],
  },
  {
    path: "/factor-manage",
    component: Layout,
    meta: { title: "因子管理", icon: "el-icon-s-tools" },
    children: [
      {
        path: "factor-manage",
        name: "factor-manage",
        component: () => import("@/views/factorManagement/index"),
        meta: { title: "因子管理", icon: "el-icon-s-tools" },
      },
          {
            path: "tool",
            name: "tool",
            component: () => import("@/views/tool/index"),
            meta: { title: "因子分析" +
                "", icon: "el-icon-s-tools" },
          }]
  },
  {
    path: "/algorithm-manage",
    component: Layout,
    children: [
      {
        path: "algorithm-manage",
        name: "algorithm-manage",
        component: () => import("@/views/algorithmManagement/index"),
        meta: { title: "算法管理", icon: "el-icon-s-tools" },
      },
    ],
  },
  {
    path: "/sceneManage",
    component: Layout,
    children: [
      {
        path: "sceneManage",
        name: "sceneManage",
        component: () => import("@/views/sceneManagement/index"),
        meta: { title: "场景管理", icon: "el-icon-data-board" },
      },
    ],
  },
  // {
  //   path: "/tool",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "tool",
  //       name: "tool",
  //       component: () => import("@/views/tool/index"),
  //       meta: { title: "因子分析" +
  //           "", icon: "el-icon-s-tools" },
  //     },
  //   ],
  // },
  //超额收益分析
  // {
  //   path: "/factor",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "factor",
  //       name: "factor",
  //       component: () => import("@/views/factor/index"),
  //       meta: { title: "因子详情页面", icon: "form" },
  //     },
  //   ],
  // },
  // {
  //   path: "/settings",
  //   component: Layout,
  //   name: "Settings",
  //   meta: { title: "个人中心", icon: "el-icon-user-solid" },
  //   children: [
  //     {
  //       path: "settings",
  //       name: "settings",
  //       component: () => import("@/views/settings/index"),
  //       meta: { title: "个人信息管理 ", icon: "el-icon-setting" },
  //     },
  //   ],
  // },

  // {
  //   path: "/market",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "market",
  //       name: "Market",
  //       component: () => import("@/views/market/index"),
  //       meta: { title: "场景超市", icon: "el-icon-goods" },
  //     },
  //   ],
  // },
  // {
  //   path: "/scenecreate",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "scenecreate",
  //       name: "Scenecreate",
  //       component: () => import("@/views/scenecreate/index"),
  //       meta: { title: "场景创建", icon: "el-icon-data-board" },
  //     },
  //   ],
  // },
  // {
  //   path: "/description",
  //   component: Layout,
  //   meta: { roles: ["editor"] },
  //   children: [
  //     {
  //       path: "",
  //       name: "Description",
  //       component: () => import("@/views/description/index"),
  //       meta: { title: "选股逻辑描述", icon: "el-icon-notebook-1" },
  //     },
  //   ],
  // },
  // {
  //   path: "/backtesting",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "",
  //       name: "Backtesting",
  //       component: () => import("@/views/backtesting/index"),
  //       meta: { title: "选股算法回测", icon: "el-icon-data-line" },
  //     },
  //   ],
  // },
  // {
  //   path: "/configuration",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "",
  //       name: "Configuration",
  //       component: () => import("@/views/configuration/index"),
  //       meta: { title: "选股算法配置", icon: "el-icon-s-marketing" },
  //     },
  //   ],
  // },
  // //策略详细信息
  // {
  //   path: "/detail",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "detail",
  //       name: "Detail",
  //       component: () => import("@/views/detail/index"),
  //       meta: { title: "策略详细信息", icon: "el-icon-document" },
  //     },
  //   ],
  // },
  //   //test
  //   {
  //     path: "/test",
  //     component: Layout,
  //     children: [
  //       {
  //         path: "test",
  //         name: "test",
  //         component: () => import("@/views/test/index"),
  //         meta: { title: "test", icon: "el-icon-document" },
  //       },
  //     ],
  //   },
  // {
  //   path: "/example",
  //   component: Layout,
  //   redirect: "/example/table",
  //   name: "Example",
  //   meta: { title: "Example", icon: "el-icon-s-help" },
  //   children: [
  //     {
  //       path: "table",
  //       name: "Table",
  //       component: () => import("@/views/table/index"),
  //       meta: { title: "Table", icon: "table" },
  //     },
  //     {
  //       path: "tree",
  //       name: "Tree",
  //       component: () => import("@/views/tree/index"),
  //       meta: { title: "Tree", icon: "tree" },
  //     },
  //   ],
  // },

  // {
  //   path: "/form",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "index",
  //       name: "Form",
  //       component: () => import("@/views/form/index"),
  //       meta: { title: "Form", icon: "form" },
  //     },
  //   ],
  // },

  // {
  //   path: "/nested",
  //   component: Layout,
  //   redirect: "/nested/menu1",
  //   name: "Nested",
  //   meta: {
  //     title: "Nested",
  //     icon: "nested",
  //   },
  //   children: [
  //     {
  //       path: "menu1",
  //       component: () => import("@/views/nested/menu1/index"), // Parent router-view
  //       name: "Menu1",
  //       meta: { title: "Menu1" },
  //       children: [
  //         {
  //           path: "menu1-1",
  //           component: () => import("@/views/nested/menu1/menu1-1"),
  //           name: "Menu1-1",
  //           meta: { title: "Menu1-1" },
  //         },
  //         {
  //           path: "menu1-2",
  //           component: () => import("@/views/nested/menu1/menu1-2"),
  //           name: "Menu1-2",
  //           meta: { title: "Menu1-2" },
  //           children: [
  //             {
  //               path: "menu1-2-1",
  //               component: () =>
  //                 import("@/views/nested/menu1/menu1-2/menu1-2-1"),
  //               name: "Menu1-2-1",
  //               meta: { title: "Menu1-2-1" },
  //             },
  //             {
  //               path: "menu1-2-2",
  //               component: () =>
  //                 import("@/views/nested/menu1/menu1-2/menu1-2-2"),
  //               name: "Menu1-2-2",
  //               meta: { title: "Menu1-2-2" },
  //             },
  //           ],
  //         },
  //         {
  //           path: "menu1-3",
  //           component: () => import("@/views/nested/menu1/menu1-3"),
  //           name: "Menu1-3",
  //           meta: { title: "Menu1-3" },
  //         },
  //       ],
  //     },
  //     {
  //       path: "menu2",
  //       component: () => import("@/views/nested/menu2/index"),
  //       name: "Menu2",
  //       meta: { title: "menu2" },
  //     },
  //   ],
  // },

  // {
  //   path: "external-link",
  //   component: Layout,
  //   children: [
  //     {
  //       path: "https://panjiachen.github.io/vue-element-admin-site/#/",
  //       meta: { title: "External Link", icon: "link" },
  //     },
  //   ],
  // },

  // 404 page must be placed at the end !!!
  { path: "*", redirect: "/404", hidden: true },
];

export const asyncRoutes = [
  {
    path: "/demo",
    component: Layout,
    children: [
      {
        path: "demo",
        name: "Demo",
        component: () => import("@/views/demo/index"),
        meta: { title: "demo", icon: "form", roles: ["editor"] },
      },
    ],
  },
];

const createRouter = () =>
  new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes,
  });

const router = createRouter();

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
