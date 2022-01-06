				<div class="modal fade" id="changePasswordModal" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" style="width: 500px !important;">
			<div class="modal-content">
				<div class="modal-header" style="color: black;">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Change Password</h4>
				</div>
				<div class="modal-body">
			  <div id="changePasswordUserDiv">			 
			   <table class="table table-default report_tables">
			   <tr>
			   <td><label>Old Password</label></td>
			   <td colspan="4">
				<input type="password" id="oldPassword" />
				</td>
			   </tr>
			   <tr>
			   <td><label>New Password</label></td>
			   <td colspan="4">
				<input type="password" id="newPassword" />
				</td>
			   </tr>
			   	<tr>
			   <td><label>Confirm Password</label></td>
			   <td colspan="4">
				<input type="password" id="confirmPassword" />
				</td>
			   </tr>					  
			 </table>
              </div> 
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" id="changePasswordButton" onclick="changePassword('any')">Change</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->