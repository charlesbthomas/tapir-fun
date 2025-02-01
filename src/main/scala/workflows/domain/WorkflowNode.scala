package dev.parvus.workflows.domain

trait WorkflowNode:
  def id: Long
  def name: String
  def description: Option[String]
  def run(): Unit
  def parent: Long
