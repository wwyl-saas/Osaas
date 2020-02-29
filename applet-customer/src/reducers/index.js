import { combineReducers } from 'redux'
import counter from './counter'
import user from './user'
import product from './product'

export default combineReducers({
  counter,
  user,
  product
})

