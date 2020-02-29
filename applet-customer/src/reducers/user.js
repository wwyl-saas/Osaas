import { PERSON_INDEX, PERSON_BILL, PERSON_WELFARE, AUTHORIZED } from '../constants/users'

const INITIAL_STATE = {
  userInfo: {},
  Authorized: false
}

export default function user(state = INITIAL_STATE, action) {
  switch(action.type) {
    case PERSON_INDEX: {
      return {
        ...state,
        userInfo: {
          ...action.payload,
          login: true
        }
      }
    }
    case PERSON_BILL: {
      return { ...state }
    }
    case PERSON_WELFARE: {
      return {
        ...state
      }
    }
    case AUTHORIZED: {
      return {
        ...state,
        Authorized: action.payload
      }
    }
    default:
      return state
  }
}
