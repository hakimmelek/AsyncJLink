package com.asyncjlink.commands;

/**
 * The user-registered local setup, as the command library sees it.
 *
 * <p>This interface exists so that {@code Commands} stays the base of the dependency graph.
 * {@code Config.PathConfig} loads {@code paths.yaml} and implements this; {@code Commands} never
 * imports {@code Config}, exactly as the architecture requires.
 */
public interface CreoPaths {

    /** {@code creo.install_dir}. */
    String creoInstallDir();

    /** {@code creo.async.pro_comm_msg_exe} — exported as {@code PRO_COMM_MSG_EXE}. */
    String proCommMsgExe();

    /** {@code creo.async.nms_port} — exported as {@code PTCNMSPORT}; must match Creo's own. */
    int nmsPort();

    /** {@code creo.async.connect.user_name}; {@code ""} = current user, {@code null} = any user. */
    String connectUserName();

    /** {@code creo.async.connect.display_name}; {@code ""} = local host, {@code null} = any display. */
    String connectDisplayName();

    /** {@code creo.async.connect.message_menu_path}; {@code null} unless external message files are used. */
    String connectMessageMenuPath();

    /** {@code creo.async.connect.timeout_seconds}. */
    int connectTimeoutSeconds();

    /** {@code export.default_output_dir} — where relative export paths land. */
    String defaultOutputDir();

    /** {@code workspace.default_dir}. */
    String defaultWorkspaceDir();
}
