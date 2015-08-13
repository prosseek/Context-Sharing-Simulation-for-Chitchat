lazy val root = (project in file(".")).aggregate(context, contextProcessor)
lazy val context = project
lazy val contextProcessor = project
