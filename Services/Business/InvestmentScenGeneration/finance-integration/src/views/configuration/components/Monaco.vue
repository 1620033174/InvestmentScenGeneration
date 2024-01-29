<template>
  <!-- width="40vw" -->
  <MonacoEditor
    height="700px"
    :theme="theme"
    language="python"
    :options="monacoOptions"
    @change="onChange"
    v-model="editorValue"
  />
</template>

<script>
import MonacoEditor from "@guolao/vue-monaco-editor";

export default {
  components: {
    MonacoEditor,
  },
  props: {
    theme: {
      type: String,
      default: "vs-dark",
      required: false,
    },
    // TODO 监听defaultValue
    monacoDefaultValue: {
      type: String,
    },
    monacoOptions: {
      type: Object,
      default: () => ({
        readOnly: false,
        foldingStrategy: "indentation", // 代码可分小段折叠
      }),
    },
  },
  data() {
    return {
      editorValue: this.monacoDefaultValue,
    };
  },
  computed: {
    readOnly() {
      return this.monacoOptions.readOnly;
    },
  },
  watch: {
    monacoDefaultValue: function () {
      this.resetValue();
    },
    readOnly: {
      handler(newVal, oldVal) {
        // console.log(oldVal, "=>", newVal, "readOnly");
        if (oldVal === false && newVal === true) {
          // 点击保存
          this.$emit("save", this.editorValue);
        }
      },
    },
  },
  methods: {
    onChange(e) {
      // console.log(e);
      this.editorValue = e;
    },
    resetValue() {
      this.editorValue = this.monacoDefaultValue;
    },
  },
};
</script>
