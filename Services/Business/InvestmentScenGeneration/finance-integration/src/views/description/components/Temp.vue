<template>
  <div>
    <div class="pageZ" v-show="page === 3">
      <article class="main">
        <div class="left-area">
          <div class="text-xl font-bold">选股因子列表</div>
          <div class="list">
            <div class="list-item" v-for="element in list" :key="element.id">
              <el-checkbox
                v-model="element.checked"
                @change="(checked) => onCheck(checked, element)"
              >
              </el-checkbox>
              <!-- TODO:暂时使用dblclick,之后等增加了预览页面时采用click -->
              <!-- popover嵌套操作+查看详细内容 -->
              <el-card class="box-card">
                <div v-html="element.content"></div>
              </el-card>
            </div>
          </div>
        </div>
        <div class="right-area">
          <div class="text-xl font-bold">选股逻辑列表</div>
          <div
            class="list-item"
            v-for="element in selectedList"
            :key="element.id"
          >
            <el-checkbox
              v-model="element.checked"
              @change="(checked) => onSelectedCheck(checked, element)"
            >
            </el-checkbox>
            <el-card
              class="box-card"
              @dblclick.native="() => editStart(element.content, element.id)"
            >
              <!-- FIXME: 此处可能有问题，等github issue -->
              <div v-html="element.content"></div>
              <!-- {{ element.content }} -->
            </el-card>
          </div>
        </div>
      </article>
      <article>
        <el-button type="primary" class="to-center">确认</el-button>
        <!-- 下方都是测试用的按钮👇 -->
        <!-- <el-button @click="show"> show now order </el-button> -->
      </article>
    </div>
    <!-- dialog等不占位的内容 -->
    <el-dialog
      title="编辑选股逻辑"
      :visible.sync="editorState.isEditing"
      :close-on-click-modal="false"
      :before-close="(hide) => beforeDialogClose(hide)"
      :destroy-on-close="true"
    >
      <editor :editorState="editorState" />
      <div slot="footer">
        <!-- <el-button class="to-right">保存</el-button> -->
        <!-- <el-button @click="() => showEditor(editorState)"
          >editorState</el-button
        > -->
        <el-button @click="() => beforeDialogClose(closeDialog)"
          >取消</el-button
        >
        <!-- 父子组件传递消息emit来在外部获取保存的值 -->
        <el-button type="primary" @click="() => saveItem()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      list: message.map((content, index) => {
        console.log("run"); // NOTE:data里面的方法只在初始化的时候运行一次
        return { content, id: index + 1, checked: false };
      }),
      selectedList: [],
      editorState: {
        id: 0,
        content: "111",
        isEditing: false,
      },
    };
  },
  show() {
    console.warn("show list");
    console.table(this.list);
    console.warn("show selectedList");
    console.table(this.selectedList);
  },
  showEditor(editorState) {
    console.log(editorState);
  },
  onCheck(checked, element) {
    // TODO: 重复代码优化, 把两个list变成一个
    if (checked === true) {
      this.selectedList.push(element);
    } else {
      const temp = this.selectedList.findIndex((cur) => cur.id === element.id);
      this.selectedList.splice(temp, 1);
    }
  },
  onSelectedCheck(checked, element) {
    if (checked === false) {
      this.selectedList.splice(
        this.selectedList.findIndex((cur) => cur.id === element.id),
        1
      );
      const temp = this.list.findIndex((cur) => cur.id === element.id);
      this.list.splice(temp, 1, {
        ...this.list[temp],
        checked: false,
      });
    }
  },

  // TODO： EditStart 和End二合一
  editStart(content, id) {
    console.log("startEdit");
    this.editorState.id = id;
    this.editorState.isEditing = true;
    this.editorState.content = content;
  },

  saveItem() {
    console.log(this.editorState.content, this.editorState.id, "save");
    // const temp = this.list.findIndex((cur) => cur.id === this.editorState.id);
    // this.list.splice(temp, 1, {
    //   ...this.list[temp],
    //   content: this.editorState.content,
    // });
    const temp2 = this.selectedList.findIndex(
      (cur) => cur.id === this.editorState.id
    );
    this.selectedList.splice(temp2, 1, {
      ...this.selectedList[temp2],
      content: this.editorState.content,
    });
    this.closeDialog();
    this.$message({
      message: "保存成功",
      type: "success",
    });
  },
  closeDialog(confirm = true) {
    this.editorState.isEditing = !confirm;
  },
  beforeDialogClose(close) {
    this.$confirm("此操作可能丢失未保存内容, 是否继续?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        close(true);
      })
      .catch(() => {
        close(false);
      });
  },
};
</script>

<style lang="scss" scoped>
.flip-list-move {
  transition: transform 0.5s;
}
.no-move {
  transition: transform 0s;
}
.ghost {
  background: #c8ebfb;
  opacity: 0.5;
}

.left-area {
  width: 570px;
  height: 65vh;
}
.right-area {
  width: 570px;
  height: 65vh;
}

.box-card {
  width: 530px;
  height: 100px;
}
.list {
  overflow-y: scroll;
  height: 60vh;

  @include scrollBar;
}

.editor {
  margin: 10px 0 0 0;
  width: 400px;
}

.edit-icon {
  cursor: pointer;
}
.handle {
  cursor: move;
}
.list-item {
  display: flex;
  align-items: center;
  margin: 10px 0;
  @include text-base;
}

//
.main {
  display: flex;
  justify-content: space-evenly;
  align-items: flex-start;
  height: 70vh;
}
</style>
