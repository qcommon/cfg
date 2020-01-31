package net.dblsaiko.qcommon.cfg.core.kotlin.api

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi
import net.dblsaiko.qcommon.cfg.core.api.cvar.ConVar
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions
import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef
import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef
import net.dblsaiko.qcommon.cfg.core.api.ref.Ref
import kotlin.reflect.KMutableProperty0

fun <T> ref(prop: KMutableProperty0<T>): Ref<T> {
  return Ref.from(prop::get, prop::set)
}

fun intRef(prop: KMutableProperty0<Int>): IntRef {
  return IntRef.from(prop::get, prop::set)
}

fun floatRef(prop: KMutableProperty0<Float>): FloatRef {
  return FloatRef.from(prop::get, prop::set)
}

fun <T : ConVar> ConfigApi.Mutable.addConVar(name: String, cvar: T, sync: Boolean? = null, save: String? = null): T {
  val options = CvarOptions.create()
  if (sync == true) options.sync()
  if (save != null) options.save(save)
  return addConVar(name, cvar, options)
}