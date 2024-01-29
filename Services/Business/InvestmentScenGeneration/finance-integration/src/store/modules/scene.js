const state = {
  scene: {
    id: 0,
    sceneName: "",
    theme: "",
    description: "",
    tags: []
  },
  date: '2020',
  previewSceneId: 0,
  dialogVisible: false,
  pictureList: [],
  stockTableData: [],
  selectedAlgorithms: []
}

const mutations = {
  SET_SCENE: (state, scene) => {
    state.scene = scene
  },
  SET_STOCK_TABLE_DATA: (state, stockTableData) => {
    state.stockTableData = stockTableData
  },
  SET_PICTURE_LIST: (state, pictureList) => {
    state.pictureList = pictureList
  },
  SET_SELECTED_ALGORITHMS: (state, selectedAlgorithms) => {
    state.selectedAlgorithms = selectedAlgorithms
  },
  SET_DIALOG_VISIBLE: (state, dialogVisible) => {
    state.dialogVisible = dialogVisible
  },
  SET_PREVIEW_SCENE_ID: (state, previewSceneId) => {
    state.previewSceneId = previewSceneId
  },
  SET_DATE: (state, date) => {
    state.date = date
  }
}

const actions = {
  setScene({ commit }, scene) {
    commit('SET_SCENE', scene)
  },
  setStockTableData({ commit }, stockTableData) {
    commit('SET_STOCK_TABLE_DATA', stockTableData)
  },
  setSelectedAlgorithms({ commit }, selectedAlgorithms) {
    commit('SET_SELECTED_ALGORITHMS', selectedAlgorithms)
  },
  setPictureList({ commit }, pictureList) {
    commit('SET_PICTURE_LIST', pictureList)
  },
  setDialogVisible({ commit }, dialogVisible) {
    commit('SET_DIALOG_VISIBLE', dialogVisible)
  },
  setPreviewSceneId({ commit }, previewSceneId) {
    commit('SET_PREVIEW_SCENE_ID', previewSceneId)
  },
  setDate({ commit }, date) {
    commit('SET_DATE', date)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
