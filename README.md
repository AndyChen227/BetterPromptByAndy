PromptOpt

  PromptOpt 是一个 模块化的 Prompt 优化引擎，用于在用户问题发送给大语言模型之前，对其进行结构化分析与自动优化。
  
  与简单的 prompt 改写工具不同，PromptOpt 通过 分析 → 构建 → 规则优化 的流水线架构，使 prompt 优化逻辑可扩展、可解释、可维护。
  
  该项目的目标是探索一种 系统化的 Prompt Engineering 方法。

  

系统架构

PromptOpt采用分层架构

用户输入 (Raw Input)
          ↓
  Analyzer（分析模块）
          ↓
  PromptBuilder（Prompt 构建）
          ↓
  Rule Engine（规则优化）
          ↓
  最终优化后的 Prompt互换
  


Analyzer

  对用户输入进行基础分析，例如：
  
  识别问题领域
  
  提取关键词
  
  判断输入类型
  
  分析结果会被后续模块使用



PromptBuilder
  
  根据分析结果生成一个 基础结构化 Prompt，通常包含：
  
    Context
    
    Task
    
    Constraints
    
    Output Format
  
  这一阶段会生成一个标准化的 Prompt 结构


Rule Engine

  Rule 模块是整个系统的 可扩展优化引擎
  
  规则系统会检测 Prompt 中可能存在的问题，并进行优化，例如：
  
  输入是否过于模糊
  
  是否缺少输出格式
  
  是否需要增加约束
  
  规则会对 Prompt 进行 逐步增强，并记录哪些规则被触发



Rule 模块设计

  Rule 模块采用 可插拔规则系统
  
  每一条规则都是独立的优化策略，并实现统一接口
  
  规则执行流程：

  Built Prompt
       ↓
  RulePipeline
       ↓
  Rule1
       ↓
  Rule2
       ↓
  Rule3
       ↓
  Final Prompt



 
规则一：

  ClarifyQuestionsRule

  
  当用户输入过于模糊时，该规则会在 Prompt 中加入澄清问题，例如：
  
  用户目标是什么
  
  面向的受众是谁
  
  需要什么输出格式


  该规则的目的是：
  
  当信息不足时，引导模型优先提出澄清问题，而不是直接猜测答案。
