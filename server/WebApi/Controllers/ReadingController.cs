using Microsoft.AspNetCore.Mvc;
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
    [Route("api/{token}/readings")]
    public class ReadingController : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly PatientRepository _patient;
        private readonly LoginRepository _login;
        private readonly ReadingRepository _reading;

        public ReadingController(ApplicationContext context)
        {
            _context = context;
            _patient = new PatientRepository(_context);
            _login = new LoginRepository(_context);
            _reading = new ReadingRepository(_context);
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

            if (IsTokenBelongsToDoctor(login.UserType))
                return Conflict(Constants.notPatient);

            if (!_patient.IsExist(login.UserId))
                return Conflict("No such patient");

            return Ok(_reading.GetAll(_patient.GetById(login.UserId).SNILS));
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

            return Ok(_reading.GetAll(snils));
#if RELEASE
            }
            catch (Exception)
            {
                return BadRequest(Constants.defaultExceptionText);
            }
#endif
        }

        [HttpPost]
        public IActionResult Create(string token, [FromBody] TonometerReadingFromUser item)
        {
#if RELEASE
            try
            {
#endif
            if (item == null || !ModelState.IsValid)
                return BadRequest(Constants.bodyItemIncorrect);

            Login login = GetLogin(token, out string error);
            if (login is null)
                return Conflict(error);

            if (IsTokenExpired(login.Expires))
                return Conflict(Constants.tokenExpired);

            if (IsTokenBelongsToDoctor(login.UserType))
                return Conflict(Constants.notPatient);

            if (!_patient.IsExist(login.UserId))
                return Conflict("No such patient");

            return Ok(_reading.Insert(new TonometerReading()
            {
                PatientSNILS = _patient.GetById(login.UserId).SNILS,
                DateTime = item.DateTime,
                SYS = item.SYS,
                DIA = item.DIA,
                Pulse = item.Pulse,
                Activity = item.Activity,
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

        [HttpPut]
        public IActionResult Edit(string token, [FromBody] TonometerReadingFromUser item)
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

            if (!_reading.IsExist(item.Id))
                return Conflict("No such tonometer reading");

            if (login.UserType == UserType.Doctor)
            {
                return Ok(_reading.Update(
                    id: item.Id, 
                    dateTime: item.DateTime,
                    SYS: item.SYS,
                    DIA: item.DIA,
                    pulse: item.Pulse,
                    activity: item.Activity,
                    comment: item.Comment));
            }
            else if (login.UserType == UserType.Patient)
            {
                if (!_patient.IsExist(login.UserId))
                    return Conflict("Token invalid");

                var reading = _reading.Get(item.Id);
                if (reading.PatientSNILS != _patient.GetById(login.UserId).SNILS)
                    return Conflict("This reading does not belongs to you");

                //if (DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond - reading.DateTime
                //    > Constants.editOrDeleteReadingTime)
                //    return Conflict("Too late to edit this reading");

                return Ok(_reading.Update(
                    id: item.Id,
                    SYS: item.SYS,
                    DIA: item.DIA,
                    pulse: item.Pulse,
                    activity: item.Activity,
                    comment: item.Comment));
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

            if (!_reading.IsExist(id))
                return Conflict("No such tonometer reading");

            if (login.UserType == UserType.Doctor)
            {
                _reading.Delete(id);
                return Ok();
            }
            else if (login.UserType == UserType.Patient)
            {
                if (!_patient.IsExist(login.UserId))
                    return Conflict("Token invalid");

                var reading = _reading.Get(id);
                if (reading.PatientSNILS != _patient.GetById(login.UserId).SNILS)
                    return Conflict("This reading does not belongs to you");

                if (DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond - reading.DateTime
                    > Constants.editOrDeleteReadingTime)
                    return Conflict("Too late to delete this reading");

                _reading.Delete(id);
                return Ok();
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
    }
}
