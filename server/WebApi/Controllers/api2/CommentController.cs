using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System;
using WebApi.Data;
using WebApi.Enums;
using WebApi.Models;
using WebApi.Models.Support;
using WebApi.Repositories;
using WebApi.Tools;

namespace WebApi.Controllers
{
    [ApiController]
    [Route("api2/{token}/comments")]
    public class CommentController2 : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly PatientRepository _patient;
        private readonly DoctorRepository _doctor;
        private readonly LoginRepository _login;
        private readonly CommentRepository _comment;

        public CommentController2(ApplicationContext context)
        {
            _context = context;
            _patient = new PatientRepository(_context);
            _login = new LoginRepository(_context);
            _comment = new CommentRepository(_context);
            _doctor = new DoctorRepository(_context);
        }

        private Login GetLogin(string token, out string error)
        {
            if (!_login.IsTokenExist(token))
            {
                error = "This token does not exist";
                return null;
            }
            error = null;
            return _login.Get(token);
        }

        private static bool IsTokenExpired(DateTime expires)
        {
            if (expires < DateTime.Now)
                return true;
            return false;
        }

        private static bool IsTokenBelongsToDoctor(UserType userType)
        {
            if (userType == UserType.Doctor)
                return true;
            return false;
        }

        [HttpGet]
        public IActionResult List(string token)
        {
#if RELEASE
            try
            {
#endif
            Login login = GetLogin(token, out string error);
            if (login is null)
                return Conflict(error);

            if (IsTokenExpired(login.Expires))
                return Conflict(Constants.tokenExpired);

            if (login.UserType == UserType.Doctor)
            {
                if (!_doctor.IsIdExist(login.UserId))
                    return Conflict("No such doctor");

                return Ok(_comment.GetAll(login.UserId));
            }
            else if (login.UserType == UserType.Patient)
            {
                if (!_patient.IsExist(login.UserId))
                    return Conflict("No such patient");

                var patientSNILS = _patient.GetById(login.UserId).SNILS;

                return Ok(_comment.GetAll(patientSNILS));
            }
            else
            {
                return NoContent();
            }
#if RELEASE
            }
            catch (Exception)
            {
                return BadRequest(Constants.defaultExceptionText);
            }
#endif
        }

        [HttpGet("{snils}")]
        public IActionResult List(string token, string snils)
        {
#if RELEASE
            try
            {
#endif
            Login login = GetLogin(token, out string error);
            if (login is null)
                return Conflict(error);

            if (IsTokenExpired(login.Expires))
                return Conflict(Constants.tokenExpired);

            if (!IsTokenBelongsToDoctor(login.UserType))
                return Conflict(Constants.notDoctor);

            if (!_patient.IsSnilsExist(snils))
                return Conflict(Constants.snilsNotExists);

            return Ok(_comment.GetAll(login.UserId, snils));
#if RELEASE
            }
            catch (Exception)
            {
                return BadRequest(Constants.defaultExceptionText);
            }
#endif
        }

        [HttpPost("{json}")]
        public IActionResult Create(string token, string json)
        {
#if RELEASE
            try
            {
#endif
            var item = JsonConvert.DeserializeObject<DoctorCommentFromUser>(json);

            if (item == null || !ModelState.IsValid)
                return BadRequest(Constants.bodyItemIncorrect);

            Login login = GetLogin(token, out string error);
            if (login is null)
                return Conflict(error);

            if (IsTokenExpired(login.Expires))
                return Conflict(Constants.tokenExpired);

            if (!IsTokenBelongsToDoctor(login.UserType))
                return Conflict(Constants.notDoctor);

            if (!_doctor.IsIdExist(login.UserId))
                return Conflict("No such doctor");

            return Ok(_comment.Insert(new DoctorComment()
            {
                DoctorId = login.UserId,
                PatientSNILS = item.PatientSNILS,
                DateTime = item.DateTime,
                Comment = item.Comment
            }));
#if RELEASE
            }
            catch (Exception)
            {
                return BadRequest(Constants.defaultExceptionText);
            }
#endif
        }

        [HttpDelete("{id}")]
        public IActionResult Delete(string token, long id)
        {
#if RELEASE
            try
            {
#endif
            var login = GetLogin(token, out string error);
            if (login is null)
                return Conflict(error);

            if (IsTokenExpired(login.Expires))
                return Conflict(Constants.tokenExpired);

            if (!_comment.IsExist(id))
                return Conflict("No such doctor comment");

            if (!IsTokenBelongsToDoctor(login.UserType))
                return Conflict(Constants.notDoctor);

            _comment.Delete(id);
            return Ok();
#if RELEASE
            }
            catch (Exception)
            {
                return BadRequest(Constants.defaultExceptionText);
            }
#endif
        }
    }
}
