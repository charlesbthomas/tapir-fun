package dev.parvus.workflows.domain

// TODO: Think about how trigger should best be abstracted
sealed trait TriggerType
case object APITrigger extends TriggerType
case object ScheduleTrigger extends TriggerType
