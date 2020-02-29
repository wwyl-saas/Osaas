import {
  CATEGORY_LIST,
  INDEX_DATA,
  INDEX_SHOP,
  APPOINTMENT_DATA,
  SETTLE_DATA,
  SET_LAT_LONG,
  SELECTED_SHOP
} from '../constants/product'

const INITIAL_STATE = {
    categoryList: [],
    indexData: {},
    indexShop: {},
    selectedShop: '',
    appointmentData: {
      arriveDate: '',	// 预约日期
      arriveTime: '',
      customerName: '',	// 预约人姓名
      goodsId: '',	// 预约项目ID
      goodsName: '到店确认',
      merchantUserId: '',	// 预约技师ID
      merchantUserName: '到店确认',
      phone: '', // 预约人手机号
      shopId: '', // 商铺ID
      shopName: '',
      shopPhone: '',
      isDetail: false
    },
    latLongData: {
      //   accuracy: 65,
      //   errMsg: "getLocation:ok",
      //   horizontalAccuracy: 65,
      //   latitude: 39.9219,
      //   longitude: 116.44355,
      //   speed: -1,
      //   verticalAccuracy: 65,
    },
    settleData: {} // socket结算数据
}

export default function product(state = INITIAL_STATE, action) {
  switch(action.type) {
    case CATEGORY_LIST: {
      return {
        ...state,
        categoryList: {
          ...action.payload
        }
      }
    }
    case INDEX_DATA: {
      return {
        ...state,
        indexData: {
          ...action.payload
        }
      }
    }
    case INDEX_SHOP: {
      return {
        ...state,
        indexShop: {
          ...action.payload
        }
      }
    }
    case APPOINTMENT_DATA: {
      return {
        ...state,
        appointmentData: {
          ...action.payload
        }
      }
    }
    case SETTLE_DATA: {
      return {
        ...state,
        settleData: {
          ...action.payload
        }
      }
    }
    case SET_LAT_LONG: {
      return {
        ...state,
        latLongData: {
          ...action.payload
        }
      }
    }
    case SELECTED_SHOP: {
      return {
        ...state,
        selectedShop: {
          ...action.payload
        }
      }
    }
    default:
      return state
  }
}
