package cn.hamm.airpower.interceptor.document;

import cn.hamm.airpower.annotation.ReadOnly;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.util.AirUtil;
import cn.hamm.airpower.validate.dictionary.Dictionary;
import cn.hamm.airpower.validate.phone.Phone;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <h1>Api文档</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
@Slf4j
public class ApiDocument {
    /**
     * <h2>标题</h2>
     */
    private String title;

    /**
     * <h2>接口文档</h2>
     */
    private String document;

    /**
     * <h2>请求参数</h2>
     */
    private List<ApiRequestParam> requestParamList = new ArrayList<>();

    /**
     * <h2>输出接口文档</h2>
     *
     * @param response 响应
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    public static void writeApiDocument(@NotNull HttpServletResponse response, Class<?> clazz, Method method) {
        ApiDocument apiDocument = new ApiDocument();
        String className = AirUtil.getReflectUtil().getDescription(clazz);
        String methodName = AirUtil.getReflectUtil().getDescription(method);
        apiDocument.setTitle(className + Constant.SPACE + methodName + " Api接口文档");

        apiDocument.setDocument(AirUtil.getReflectUtil().getDocument(method));

        apiDocument.setRequestParamList(getRequestParamList(clazz, method));

        String json = Json.toString(apiDocument);


        String html = """
                <!DOCTYPE html>
                <html>
                  <head>
                      <title>AirPower4J 接口文档</title>
                      <meta name="referrer" content="never">
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
                      <link rel="stylesheet" href="//at.alicdn.com/t/c/font_666204_kf3f5tzpd8f.css">
                      <link rel="stylesheet" href="//cdn.hamm.cn/css/element.css">
                      <link rel="icon" href="//cdn.hamm.cn/favicon.ico">
                      <style>
                      .dictionary{
                          display: flex;
                          flex-direction: row;
                          align-items: center;
                          min-width: 200px;
                      }
                      .dictionary .key{
                          color: red;
                      }
                      .dictionary .label{
                          flex: 1;
                          width: 0;
                      }
                      .body{
                          position: absolute;
                          left: 10%;
                          right: 10%;
                          top: 0;
                          bottom: 0;
                          display: flex;
                          flex-direction: column;
                          padding: 40px;
                      }
                      .body .card{
                          flex: 1;
                          height: 0;
                          overflow: hidden;
                          overflow-y: auto;
                      }
                      .body .header{
                          font-size: 24px;
                          font-weight: bold;
                          padding: 20px 0px;
                      }
                      .body .desc{
                          font-size: 14px;
                          color: #999;
                          background: #f5f5f5;
                          padding: 10px 20px;
                          margin-bottom: 20px;
                          border-radius: 8px;
                      }
                      h2 {
                          font-size: 16px;
                          font-weight: bold;
                          margin-top: 40px;
                      }
                      .content{
                          display: flex;
                          flex-direction: row;
                          align-items: center;
                      }
                      .content .method{
                          background: #666;
                          color: white;
                          padding: 2px 8px;
                          font-size: 12px;
                          border-radius: 5px;
                      }
                      .content .url{
                          margin-left: 20px;
                          color: #159;
                      }
                      a{
                        color: orangered;
                        text-decoration: none;
                      }
                      </style>
                  </head>
                  <body>
                      <div id="app" v-cloak>
                          <div class="body">
                              <div class="header">{{api.title}} <a href="javascript:history.go(-1);"> 返回 </a></div>
                              <div class="desc" v-if="api.document">{{api.document}}</div>
                              <div class="card">
                                  <h2>请求方式</h2>
                                  <div class="content">
                                      <div class="method">POST</div> <div class="url">{{url}}</div>
                                  </div>
                                  <h2>请求参数</h2>
                                  <el-table class="table" stripe size="medium" :data="api.requestParamList" default-expand-all
                                   :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                                      <el-table-column prop="name" label="参数Key" ></el-table-column>
                                      <el-table-column prop="description" label="参数名称" width="200"></el-table-column>
                                      <el-table-column prop="type" width="150" label="数据类型">
                                          <template slot-scope="scope">
                                            <el-link :href="scope.row.link" v-if="scope.row.link">{{ scope.row.type }}</el-link>
                                            <template v-else>{{ scope.row.type }}</template>
                                          </template>
                                      </el-table-column>
                                      <el-table-column label="其他说明">
                                          <template slot-scope="scope">
                                              <el-tag size="small" v-if="scope.row.required">必填</el-tag>
                                              <el-tag size="small" v-if="scope.row.phone">电话</el-tag>
                                              <el-tag size="small" v-if="scope.row.email">邮箱</el-tag>
                                              <el-dropdown v-if="scope.row.dictionary.length>0">
                                                  <el-tag size="small">字典</el-tag>
                                                  <el-dropdown-menu slot="dropdown">
                                                      <el-dropdown-item v-for="item in scope.row.dictionary">
                                                          <div class="dictionary">
                                                              <div class="label">{{item.label}}</div>
                                                              <div class="key">{{item.key}}</div>
                                                          </div>
                                                      </el-dropdown-item>
                                                  </el-dropdown-menu>
                                              </el-dropdown>
                                          </template>
                                      </el-table-column>
                                      <el-table-column prop="document" label="备注说明"></el-table-column>
                                  </el-table>
                              </div>
                          </div>
                      </div>
                  </body>
                  <script src="//cdn.hamm.cn/js/vue-2.6.10.min.js"></script>
                  <script src="//cdn.hamm.cn/js/axios.min.js"></script>
                  <script src="//cdn.hamm.cn/js/element.js"></script>
                  <script src="//cdn.hamm.cn/js/vue-clipboard.min.js"></script>
                  <script>
                  const json =
                """
                + json +
                """
                           </script>
                           <script>
                           new Vue({
                               el: '#app',
                               data() {
                                   return {
                                       url: window.location.pathname,
                                       api: json,
                                   }
                               },
                               created() {
                                   console.log(this.api)
                                   window.document.title = this.api.title + " - AirPower4J"
                                   this.api.requestParamList.sort((a,b)=>{
                                               if(a.required){
                                                   return -1;
                                               }
                                               if(b.required){
                                                   return 1;
                                               }
                                           })
                               },
                               updated() {},
                               methods: {}
                           });
                           </script>
                        </html>
                        """;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(html);
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    /**
     * <h2>获取请求参数</h2>
     *
     * @param currentClass 类
     * @param method       方法
     * @return 请求参数列表
     */
    private static @NotNull List<ApiRequestParam> getRequestParamList(Class<?> currentClass, @NotNull Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return new ArrayList<>();
        }
        Parameter parameter = parameters[0];

        List<ApiRequestParam> params = new ArrayList<>();
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        if (Objects.isNull(requestBody)) {
            return params;
        }

        Class<?> action = Void.class;
        Validated validated = parameter.getAnnotation(Validated.class);
        if (Objects.nonNull(validated)) {
            if (validated.value().length == 0) {
                return params;
            }
            action = validated.value()[0];
        }

        Class<?> paramClass = parameter.getType();
        if (!parameter.getParameterizedType().getTypeName().contains(Constant.DOT)) {
            // 泛型
            paramClass = (Class<?>) ((((ParameterizedType) currentClass
                    .getGenericSuperclass())
                    .getActualTypeArguments())[0]);
        }

        List<Field> fields = AirUtil.getReflectUtil().getFieldList(paramClass);

        return getFieldList(fields, currentClass, action);
    }

    private static @NotNull List<ApiRequestParam> getFieldList(
            @NotNull List<Field> fields, Class<?> currentClass, Class<?> action
    ) {
        List<ApiRequestParam> params = new ArrayList<>();
        for (Field field : fields) {
            ReadOnly readOnly = AirUtil.getReflectUtil().getAnnotation(ReadOnly.class, field);
            if (Objects.nonNull(readOnly)) {
                continue;
            }
            ApiRequestParam apiRequestParam = new ApiRequestParam();
            apiRequestParam.setName(field.getName());
            apiRequestParam.setDescription(AirUtil.getReflectUtil().getDescription(field));
            apiRequestParam.setDocument(AirUtil.getReflectUtil().getDocument(field));
            apiRequestParam.setType(field.getType().getSimpleName());
            if (AirUtil.getReflectUtil().isModel(field.getType())) {
                apiRequestParam.setLink(field.getType().getName());
            }

            // 获取字段的泛型类型
            if (!field.getGenericType().getTypeName().contains(Constant.DOT)) {
                Class<?> clazz = (Class<?>) (((ParameterizedType) currentClass
                        .getGenericSuperclass())
                        .getActualTypeArguments())[0];
                apiRequestParam.setType(clazz.getSimpleName());
                if (AirUtil.getReflectUtil().isModel(clazz)) {
                    apiRequestParam.setLink(clazz.getName());
                }
            }

            jakarta.validation.constraints.NotNull notNull = AirUtil.getReflectUtil().getAnnotation(
                    jakarta.validation.constraints.NotNull.class, field
            );
            NotBlank notBlank = AirUtil.getReflectUtil().getAnnotation(NotBlank.class, field);

            if (!action.equals(Void.class)) {
                if (Objects.nonNull(notBlank) && Arrays.stream(notBlank.groups()).toList().contains(action)) {
                    apiRequestParam.setRequired(true);
                }
                if (Objects.nonNull(notNull) && Arrays.stream(notNull.groups()).toList().contains(action)) {
                    apiRequestParam.setRequired(true);
                }
            }

            Dictionary dictionary = AirUtil.getReflectUtil().getAnnotation(Dictionary.class, field);
            if (Objects.nonNull(dictionary) && Arrays.stream(dictionary.groups()).toList().contains(action)) {
                apiRequestParam.setDictionary(AirUtil.getDictionaryUtil().getDictionaryList(dictionary.value()));
            }

            Phone phone = AirUtil.getReflectUtil().getAnnotation(Phone.class, field);
            if (Objects.nonNull(phone) && Arrays.stream(phone.groups()).toList().contains(action)) {
                if (phone.mobile() || phone.tel()) {
                    apiRequestParam.setPhone(true);
                }
            }

            Email email = AirUtil.getReflectUtil().getAnnotation(Email.class, field);
            if (Objects.nonNull(email) && Arrays.stream(email.groups()).toList().contains(action)) {
                apiRequestParam.setEmail(true);
            }
            params.add(apiRequestParam);
        }
        return params;
    }

    @SuppressWarnings("AlibabaMethodTooLong")
    public static boolean writeEntityDocument(String packageName, HttpServletResponse response) {
        System.out.println(packageName);
        try {
            Class<?> clazz = Class.forName(packageName);
            if (!AirUtil.getReflectUtil().isModel(clazz)) {
                return false;
            }
            List<ApiRequestParam> params = getFieldList(AirUtil.getReflectUtil().getFieldList(clazz), clazz, Void.class);

            ApiDocument apiDocument = new ApiDocument();
            apiDocument.setTitle(AirUtil.getReflectUtil().getDescription(clazz) + " " + clazz.getSimpleName());

            apiDocument.setDocument(AirUtil.getReflectUtil().getDocument(clazz));

            apiDocument.setRequestParamList(params);


            String json = Json.toString(apiDocument);

            String html = """
                    <!DOCTYPE html>
                    <html>
                      <head>
                          <title>AirPower4J 类文档</title>
                          <meta name="referrer" content="never">
                          <meta charset="UTF-8">
                          <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
                          <link rel="stylesheet" href="//at.alicdn.com/t/c/font_666204_kf3f5tzpd8f.css">
                          <link rel="stylesheet" href="//cdn.hamm.cn/css/element.css">
                          <link rel="icon" href="//cdn.hamm.cn/favicon.ico">
                          <style>
                          .dictionary{
                              display: flex;
                              flex-direction: row;
                              align-items: center;
                              min-width: 200px;
                          }
                          .dictionary .key{
                              color: red;
                          }
                          .dictionary .label{
                              flex: 1;
                              width: 0;
                          }
                          .body{
                              position: absolute;
                              left: 10%;
                              right: 10%;
                              top: 0;
                              bottom: 0;
                              display: flex;
                              flex-direction: column;
                              padding: 40px;
                          }
                          .body .card{
                              flex: 1;
                              height: 0;
                              overflow: hidden;
                              overflow-y: auto;
                          }
                          .body .header{
                              font-size: 24px;
                              font-weight: bold;
                              padding: 20px 0px;
                          }
                          .body .desc{
                              font-size: 14px;
                              color: #999;
                              background: #f5f5f5;
                              padding: 10px 20px;
                              margin-bottom: 20px;
                              border-radius: 8px;
                          }
                          h2 {
                              font-size: 16px;
                              font-weight: bold;
                              margin-top: 40px;
                          }
                          .content{
                              display: flex;
                              flex-direction: row;
                              align-items: center;
                          }
                          .content .method{
                              background: #666;
                              color: white;
                              padding: 2px 8px;
                              font-size: 12px;
                              border-radius: 5px;
                          }
                          .content .url{
                              margin-left: 20px;
                              color: #159;
                          }
                          a{
                            color: orangered;
                            text-decoration: none;
                          }
                          </style>
                      </head>
                      <body>
                          <div id="app" v-cloak>
                              <div class="body">
                                <div class="header">{{api.title}} <a href="javascript:history.go(-1);"> 返回 </a></div>
                                  <div class="desc" v-if="api.document">{{api.document}}</div>
                                  <div class="card">
                                      <h2>属性列表</h2>
                                      <el-table class="table" stripe size="medium" :data="api.requestParamList" default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                                          <el-table-column prop="name" label="属性" ></el-table-column>
                                          <el-table-column prop="description" label="属性说明" width="200"></el-table-column>
                                          <el-table-column prop="type" width="150" label="数据类型">
                                              <template slot-scope="scope">
                                                <el-link :href="scope.row.link" v-if="scope.row.link">{{ scope.row.type }}</el-link>
                                                <template v-else>{{ scope.row.type }}</template>
                                              </template>
                                          </el-table-column>
                                          <el-table-column label="其他说明">
                                              <template slot-scope="scope">
                                                  <el-tag size="small" v-if="scope.row.required">必填</el-tag>
                                                  <el-tag size="small" v-if="scope.row.phone">电话</el-tag>
                                                  <el-tag size="small" v-if="scope.row.email">邮箱</el-tag>
                                                  <el-dropdown v-if="scope.row.dictionary.length>0">
                                                      <el-tag size="small">字典</el-tag>
                                                      <el-dropdown-menu slot="dropdown">
                                                          <el-dropdown-item v-for="item in scope.row.dictionary">
                                                              <div class="dictionary">
                                                                  <div class="label">{{item.label}}</div>
                                                                  <div class="key">{{item.key}}</div>
                                                              </div>
                                                          </el-dropdown-item>
                                                      </el-dropdown-menu>
                                                  </el-dropdown>
                                              </template>
                                          </el-table-column>
                                          <el-table-column prop="document" label="备注说明"></el-table-column>
                                      </el-table>
                                  </div>
                              </div>
                          </div>
                      </body>
                      <script src="//cdn.hamm.cn/js/vue-2.6.10.min.js"></script>
                      <script src="//cdn.hamm.cn/js/axios.min.js"></script>
                      <script src="//cdn.hamm.cn/js/element.js"></script>
                      <script src="//cdn.hamm.cn/js/vue-clipboard.min.js"></script>
                      <script>
                      const json =
                    """
                    + json +
                    """
                               </script>
                               <script>
                               new Vue({
                                   el: '#app',
                                   data() {
                                       return {
                                           url: window.location.pathname,
                                           api: json,
                                       }
                                   },
                                   created() {
                                       console.log(this.api)
                                   },
                                   updated() {},
                                   methods: {}
                               });
                               </script>
                            </html>
                            """;
            try {
                response.reset();
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(html);
                response.flushBuffer();
            } catch (IOException exception) {
                log.error("输出文档失败", exception);
            }
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    @Data
    @Accessors(chain = true)
    static class ApiRequestParam {
        private String name;
        private String type;
        private String link;
        private String description;
        private String document;
        private Boolean required = false;
        private List<Map<String, String>> dictionary = new ArrayList<>();
        private List<ApiRequestParam> children = new ArrayList<>();
        private Boolean phone = false;
        private Boolean email = false;
        private Integer maxLength = 0;
    }
}

